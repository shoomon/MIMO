import { create } from "zustand";
import { OpenVidu, Session, Publisher, Subscriber} from "openvidu-browser";

interface OpenViduState {

  OV: OpenVidu | null;
  session: Session | null;
  publisher: Publisher | null;
  subscribers: Subscriber[];
  connectionStatus : "disconnected" | "connecting" | "connected" | "error";
  error: Error | null;

  videoEnabled: boolean;
  screenSharing: boolean;

  initializeSession: (sessionId: string, token: string) => Promise<void>;
  publishStream: () => Promise<void>;
  leaveSession: () => void;
  toggleAudio: () => void;
  toggleVideo: () => void;
}

const useOpenViduStore = create<OpenViduState>((set, get) => ({

  OV: null,
  session: null,
  publisher: null,
  subscribers: [],
  connectionStatus: "disconnected" as const,
  error: null,
  

}))