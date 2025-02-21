import { create } from "zustand";
import { OpenVidu, Session, Publisher, Subscriber, StreamManager, Device} from "openvidu-browser";
import { customFetch } from "@/apis/customFetch";

interface OpenViduState {
  // session 관련
  OV: OpenVidu | null;
  session: Session | null;
  mySessionId: string;
  myUserName: string | null;

  // stream 관련
  mainStreamManager: StreamManager | undefined;
  publisher: Publisher | undefined;
  subscribers: Subscriber[];
  currentVideoDevice: Device | undefined;

  // 기능 상태
  videoEnabled: boolean;
  screenSharing: boolean;

  // action
  handleChangeSessionId: (sessionId: string) => void;
  handleChangeUserName: (userName: string) => void;
  handleMainVideoStream: (stream: StreamManager) => void;
  joinSession: () => Promise<void>;
  leaveSession: () => void;
  switchCamera: () => Promise<void>;
  toggleVideo: () => void;
  toggleScreenShare: () => Promise<void>;
}

const useOpenViduStore = create<OpenViduState>((set, get) => ({

  // 초기 상태
  OV: null,
  session: null,
  mySessionId: "SessionA",
  myUserName: null,
  mainStreamManager: undefined,
  publisher: undefined,
  subscribers: [],
  currentVideoDevice: undefined,
  videoEnabled: true,
  screenSharing: false,

  handleChangeSessionId: (sessionId: string) => {
    set({mySessionId: sessionId});
  },

  handleChangeUserName: (userName: string) => {
    set({myUserName: userName});
  },

  handleMainVideoStream: (stream: StreamManager) => {
    set({mainStreamManager: stream});
  },

  // Session 관련 메서드
  joinSession: async () => {
    try {
      const OV = new OpenVidu();
      const session = OV.initSession();

      // Stream 이벤트 핸들러
      session.on("streamCreated", (event) => {
        const subscriber = session.subscribe(event.stream, undefined);
        set((state) => ({
          subscribers: [...state.subscribers, subscriber],
        }));
      });

      session.on("streamDestroyed", (event) => {
        set((state) => ({
          subscribers: state.subscribers.filter(
            (sub) => sub !== event.stream.streamManager
          ),
        }));
      });

      session.on("exception", (exception) => {
        console.warn(exception);
      });

      set({ OV, session });

      // Token 발급 및 세션 연결
      const token = await getToken(get().mySessionId);
      await session.connect(token, { clientData: get().myUserName });

      // Publisher 초기화
      const publisher = await OV.initPublisherAsync(undefined, {
        audioSource: undefined,
        videoSource: undefined,
        publishAudio: true,
        publishVideo: true,
        resolution: "640x480",
        frameRate: 30,
        insertMode: "APPEND",
        mirror: false,
      });

      await session.publish(publisher);

      // 비디오 디바이스 설정
      const devices = await OV.getDevices();
      const videoDevices = devices.filter(
        (device) => device.kind === "videoinput"
      );
      const currentVideoDeviceId = publisher.stream
        .getMediaStream()
        .getVideoTracks()[0]
        .getSettings().deviceId;
      const currentVideoDevice = videoDevices.find(
        (device) => device.deviceId === currentVideoDeviceId
      );

      set({
        currentVideoDevice,
        mainStreamManager: publisher,
        publisher,
      });
    } catch (error) {
      console.error("Error in joinSession:", error);
    }
  },

  leaveSession: () => {
    const { session } = get();
    if (session) {
      session.disconnect();
    }

    set({
      OV: null,
      session: null,
      subscribers: [],
      mySessionId: "SessionA",
      myUserName: `Participant${Math.floor(Math.random() * 100)}`,
      mainStreamManager: undefined,
      publisher: undefined,
      screenSharing: false,
      videoEnabled: true,
    });
  },

  // 카메라 제어
  switchCamera: async () => {
    const { OV, session, currentVideoDevice } = get();
    if (!OV || !session) return;

    try {
      const devices = await OV.getDevices();
      const videoDevices = devices.filter(
        (device) => device.kind === "videoinput"
      );

      if (videoDevices.length > 1) {
        const newVideoDevice = videoDevices.find(
          (device) => device.deviceId !== currentVideoDevice?.deviceId
        );

        if (newVideoDevice) {
          const newPublisher = OV.initPublisher(undefined, {
            videoSource: newVideoDevice.deviceId,
            publishAudio: true,
            publishVideo: true,
            mirror: true,
          });

          await session.unpublish(get().mainStreamManager as Publisher);
          await session.publish(newPublisher);

          set({
            currentVideoDevice: newVideoDevice,
            mainStreamManager: newPublisher,
            publisher: newPublisher,
          });
        }
      }
    } catch (error) {
      console.error("Error switching camera:", error);
    }
  },

  toggleVideo: () => {
    const { publisher } = get();
    if (publisher) {
      const videoEnabled = !publisher.stream.videoActive;
      publisher.publishVideo(videoEnabled);
      set({ videoEnabled });
    }
  },

  toggleScreenShare: async () => {
    const { OV, session, screenSharing } = get();
    if (!OV || !session) return;

    try {
      if (!screenSharing) {
        const screenPublisher = await OV.initPublisherAsync(undefined, {
          videoSource: "screen",
          publishAudio: true,
          publishVideo: true,
          mirror: false,
        });

        await session.unpublish(get().publisher as Publisher);
        await session.publish(screenPublisher);

        set({
          screenSharing: true,
          mainStreamManager: screenPublisher,
          publisher: screenPublisher,
        });
      } else {
        const cameraPublisher = await OV.initPublisherAsync(undefined, {
          videoSource: undefined,
          publishAudio: true,
          publishVideo: true,
          mirror: true,
        });

        await session.unpublish(get().publisher as Publisher);
        await session.publish(cameraPublisher);

        set({
          screenSharing: false,
          mainStreamManager: cameraPublisher,
          publisher: cameraPublisher,
        });
      }
    } catch (error) {
      console.error("Error toggling screen share:", error);
    }
  },
  
}));

async function getToken(sessionID: string){
  const session = await createSession(sessionID);

  return await createToken(session);
}

async function createSession(sessionId: string){

  const response = await customFetch("/openvidu/sessions", {
    method: "POST",
    body: JSON.stringify({customSessionId: sessionId}),
  });

  if(!response.ok){
    throw new Error("Error creating session ");
  }

  return await response.json();

}

async function createToken(sessionId: string){
  const response = await customFetch(`/openvidu/sessions/${sessionId}/connections`, {
    method: "POST",
    body: JSON.stringify({}),
  })

  if(!response.ok){
    throw new Error("Error creating token ");
  }

  return await response.json();
}

export default useOpenViduStore;