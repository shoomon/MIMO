import { StreamManager } from "openvidu-browser";
import { useEffect, useRef } from "react";

interface UserVideoProps {
  streamManager: StreamManager;
}

const UserVideo = ({streamManager}: UserVideoProps) => {

  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    if (streamManager && videoRef.current) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, [streamManager]);

  const getNicknameTag = () => {
    if (streamManager) {
      return JSON.parse(streamManager.stream.connection.data).clientData;
    }
  };

  return (
    <div className="relative bg-gray-100 rounded-lg overflow-hidden min-w-[300px] min-h-[300px]">
  <div className="relative w-full h-full">
    <video
      autoPlay={true}
      ref={videoRef}
      className={`w-full h-auto rounded-lg ${
        !streamManager.stream.videoActive ? 'bg-gray-900' : ''
      }`}
    />
    {!streamManager.stream.videoActive && (
      <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-black/50 text-white px-4 py-2 rounded">
        <span>Video Off</span>
      </div>
    )}
  </div>
  
  {/* 닉네임 표시 */}
  <div className="absolute bottom-2 left-2 z-10 bg-black/50 text-white px-2 py-1 rounded text-sm">
    <span>{getNicknameTag()}</span>
  </div>
  
  {/* 오디오 상태 표시 */}
  <div className="absolute top-2 right-2 z-10 bg-black/50 p-1 rounded-full">
    {streamManager.stream.audioActive ? (
      <span className="text-green-500">🎤</span>
    ) : (
      <span className="text-red-500">🔇</span>
    )}
  </div>
</div>
  )
}

export default UserVideo;