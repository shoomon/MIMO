import { create } from "zustand";
import { OpenVidu, Session, Publisher, Subscriber, StreamManager, Device} from "openvidu-browser";
import { createSessionAPI, createViduTokenAPI } from "@/apis/ViduApi";

interface OpenViduState {
  // session 관련
  OV: OpenVidu | null;
  session: Session | null;
  mySessionId: string;
  myUserName: string;

  // stream 관련
  mainStreamManager: StreamManager | undefined;
  publisher: Publisher | undefined;
  subscribers: Subscriber[];
  currentVideoDevice: Device | undefined;

  // 기능 상태
  videoEnabled: boolean;
  screenSharing: boolean;

  // action
  initializeSession: () => Promise<void>;
  leaveSession: () => void;
  updateSessionId: (sessionId: string) => void;
  updateUserName: (userName: string) => void;
  handleMainVideoStream: (stream: StreamManager) => void;
  switchCamera: () => Promise<void>;
  toggleVideo: () => void;
  toggleScreenShare: () => Promise<void>;
}

const useOpenViduStore = create<OpenViduState>((set, get) => ({

  OV: null,
  session: null,
  mySessionId: "",
  myUserName: "",
  mainStreamManager: undefined,
  publisher: undefined,
  subscribers: [],
  currentVideoDevice: undefined,
  videoEnabled: true,
  screenSharing: false,

  initializeSession: async () => {
    const OV = new OpenVidu();
    const session = OV.initSession();

    session.on("streamCreated", (e) => {
      const subscriber = session.subscribe(e.stream, undefined);
      set((state) => ({
        subscribers: [...state.subscribers, subscriber]
      }))
    })

    session.on("streamDestroyed", (e) => {
      set(state => ({
        subscribers: state.subscribers.filter(sub => sub !== e.stream.streamManager)
      }))
    })

    session.on("exception", (exception) => {
      console.warn(exception);
    })

    set({OV, session});

    const { mySessionId } = get();

    try{

      const sessionId = await createSessionAPI(mySessionId);
      const token = await createViduTokenAPI(sessionId);

      await session.connect(token, {clientData: get().myUserName});

      const publisher = await OV.initPublisherAsync(undefined, {
        audioSource: undefined,
        videoSource: undefined,
        publishAudio: true,
        publishVideo: true,
        resolution: "640x480",
        frameRate: 30,
        insertMode: "APPEND",
        mirror: false,
      })

      await session.publish(publisher);

      const devices = await OV.getDevices();
      const videoDevices = devices.filter(device => device.kind === "videoinput");
      const currentVideoDeviceId = publisher.stream
        .getMediaStream()
        .getVideoTracks()[0]
        .getSettings().deviceId;
      
      const currentVideoDevice = videoDevices.find(device => device.deviceId === currentVideoDeviceId);
    
      set({ currentVideoDevice, mainStreamManager:publisher, publisher})
    }catch(error){
      console.error("error in initializerSession: ", error);
    }
  },

  leaveSession: () => {
    const { session } = get();
    if(session){
      session.disconnect();
    }

    set({
      OV: null,
      session: undefined,
      subscribers: [],
      mySessionId: 'SessionA',
      myUserName: `undefined`,
      mainStreamManager: undefined,
      publisher: undefined,
      screenSharing: false,
      videoEnabled: true
    })
  },

  updateSessionId: (sessionId: string) => {
    set({mySessionId: sessionId});
  },

  updateUserName: (userName: string) => {
    set({myUserName: userName});
  },
  handleMainVideoStream: (stream: StreamManager) => {
    set({mainStreamManager: stream});
  },
  switchCamera: async () => {
    const { OV, session, currentVideoDevice } = get();
    if (!OV || !session) return;

    try {
      const devices = await OV.getDevices();
      const videoDevices = devices.filter(device => device.kind === 'videoinput');

      if (videoDevices.length > 1) {
        const newVideoDevice = videoDevices.find(
          device => device.deviceId !== currentVideoDevice?.deviceId
        );

        if (newVideoDevice) {
          const newPublisher = OV.initPublisher(undefined, {
            videoSource: newVideoDevice.deviceId,
            publishAudio: true,
            publishVideo: true,
            mirror: true
          });

          await session.unpublish(get().mainStreamManager as Publisher);
          await session.publish(newPublisher);

          set({
            currentVideoDevice: newVideoDevice,
            mainStreamManager: newPublisher,
            publisher: newPublisher
          });
        }
      }
    } catch (error) {
      console.error('Error switching camera:', error);
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
          videoSource: 'screen',
          publishAudio: true,
          publishVideo: true,
          mirror: false
        });

        await session.unpublish(get().publisher as Publisher);
        await session.publish(screenPublisher);

        set({
          screenSharing: true,
          mainStreamManager: screenPublisher,
          publisher: screenPublisher
        });
      } else {
        const cameraPublisher = await OV.initPublisherAsync(undefined, {
          videoSource: undefined,
          publishAudio: true,
          publishVideo: true,
          mirror: true
        });

        await session.unpublish(get().publisher as Publisher);
        await session.publish(cameraPublisher);

        set({
          screenSharing: false,
          mainStreamManager: cameraPublisher,
          publisher: cameraPublisher
        });
      }
    } catch (error) {
      console.error('Error toggling screen share:', error);
    }
  }
}))

export default useOpenViduStore;