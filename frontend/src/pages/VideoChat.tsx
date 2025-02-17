import UserVideo from "@/components/molecules/UserVideo/UserVideo";
import useOpenViduStore from "@/stores/viduStore";
import { useEffect } from "react";

const VideoChat = () => {
    const {
      session,
      mySessionId,
      myUserName,
      mainStreamManager,
      publisher,
      subscribers,
      videoEnabled,
      screenSharing,
      initializeSession,
      leaveSession,
      updateSessionId,
      updateUserName,
      handleMainVideoStream,
      switchCamera,
      toggleVideo,
      toggleScreenShare
    } = useOpenViduStore();
  
    useEffect(() => {
      const onBeforeUnload = () => {
        leaveSession();
      };
      window.addEventListener('beforeunload', onBeforeUnload);
      
      return () => {
        window.removeEventListener('beforeunload', onBeforeUnload);
        leaveSession();
      };
    }, [leaveSession]);
  
    const handleJoinSession = (e: React.FormEvent) => {
      e.preventDefault();
      initializeSession();
    };
  
    return (
      <div className="container">
        {session === undefined ? (
          <div id="join">
            <div id="join-dialog" className="jumbotron vertical-center">
              <h1>Join a video session</h1>
              <form className="form-group" onSubmit={handleJoinSession}>
                <p>
                  <label>Participant: </label>
                  <input
                    className="form-control"
                    type="text"
                    id="userName"
                    value={myUserName}
                    onChange={(e) => updateUserName(e.target.value)}
                    required
                  />
                </p>
                <p>
                  <label>Session: </label>
                  <input
                    className="form-control"
                    type="text"
                    id="sessionId"
                    value={mySessionId}
                    onChange={(e) => updateSessionId(e.target.value)}
                    required
                  />
                </p>
                <p className="text-center">
                  <input
                    className="btn btn-lg btn-success"
                    name="commit"
                    type="submit"
                    value="JOIN"
                  />
                </p>
              </form>
            </div>
          </div>
        ) : null}
  
        {session !== undefined ? (
          <div id="session">
            <div id="session-header">
              <h1 id="session-title">{mySessionId}</h1>
              <input
                className="btn btn-large btn-danger"
                type="button"
                id="buttonLeaveSession"
                onClick={leaveSession}
                value="Leave session"
              />
              <input
                className="btn btn-large btn-success"
                type="button"
                id="buttonSwitchCamera"
                onClick={switchCamera}
                value="Switch Camera"
              />
              <input
                className="btn btn-large btn-warning"
                type="button"
                id="buttonToggleVideo"
                onClick={toggleVideo}
                value={videoEnabled ? "카메라 끄기" : "카메라 켜기"}
              />
              <input
                className="btn btn-large btn-info"
                type="button"
                id="buttonToggleScreenShare"
                onClick={toggleScreenShare}
                value={screenSharing ? "화면 공유 중지" : "화면 공유 시작"}
              />
            </div>
  
            {mainStreamManager && (
              <div id="main-video" className="col-md-6">
                <UserVideo streamManager={mainStreamManager} />
              </div>
            )}
  
            <div id="video-container" className="col-md-6">
              {publisher && (
                <div
                  className="stream-container col-md-6 col-xs-6"
                  onClick={() => handleMainVideoStream(publisher)}
                >
                  <UserVideo streamManager={publisher} />
                </div>
              )}
              {subscribers.map((sub, i) => (
                <div
                  key={i}
                  className="stream-container col-md-6 col-xs-6"
                  onClick={() => handleMainVideoStream(sub)}
                >
                  <span>{sub.id}</span>
                  <UserVideo streamManager={sub} />
                </div>
              ))}
            </div>
          </div>
        ) : null}
      </div>
    );
  };
export default VideoChat;
