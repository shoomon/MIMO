// components/OpenViduPage.tsx
import React, { useEffect } from 'react';
import useOpenViduStore from '@/store/useOpenViduStore';
import UserVideoComponent from './UserVideoComponent';

const OpenViduPage = () => {
    const {
        session,
        mySessionId,
        myUserName,
        mainStreamManager,
        publisher,
        subscribers,
        videoEnabled,
        screenSharing,
        // actions
        handleChangeSessionId,
        handleChangeUserName,
        handleMainVideoStream,
        joinSession,
        leaveSession,
        switchCamera,
        toggleVideo,
        toggleScreenShare,
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

    const handleJoinSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        joinSession();
    };

    return (
        <div className="min-h-screen w-full bg-gray-100">
            {!session ? (
                // Join Form
                <div className="flex min-h-screen items-center justify-center p-4">
                    <div className="w-full max-w-md rounded-lg bg-white p-8 shadow-lg">
                        <h1 className="mb-6 text-center text-2xl font-bold">
                            Join Video Session
                        </h1>
                        <form onSubmit={handleJoinSubmit} className="space-y-6">
                            <div>
                                <label
                                    htmlFor="userName"
                                    className="mb-2 block text-sm font-medium text-gray-700"
                                >
                                    Participant Name
                                </label>
                                <input
                                    id="userName"
                                    type="text"
                                    value={myUserName || ''}
                                    onChange={(e) =>
                                        handleChangeUserName(e.target.value)
                                    }
                                    className="w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                                    required
                                />
                            </div>
                            <div>
                                <label
                                    htmlFor="sessionId"
                                    className="mb-2 block text-sm font-medium text-gray-700"
                                >
                                    Session ID
                                </label>
                                <input
                                    id="sessionId"
                                    type="text"
                                    value={mySessionId}
                                    onChange={(e) =>
                                        handleChangeSessionId(e.target.value)
                                    }
                                    className="w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                                    required
                                />
                            </div>
                            <button
                                type="submit"
                                className="w-full rounded-md bg-indigo-600 px-4 py-2 text-white hover:bg-indigo-700 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-none"
                            >
                                Join Session
                            </button>
                        </form>
                    </div>
                </div>
            ) : (
                // Video Session
                <div className="p-4">
                    {/* Session Header */}
                    <div className="mb-4 rounded-lg bg-white p-4 shadow-md">
                        <div className="flex flex-wrap items-center justify-between gap-4">
                            <h1 className="text-xl font-bold">
                                Session: {mySessionId}
                            </h1>
                            <div className="flex flex-wrap gap-2">
                                <button
                                    onClick={leaveSession}
                                    className="rounded bg-red-600 px-4 py-2 text-white hover:bg-red-700"
                                >
                                    Leave Session
                                </button>
                                <button
                                    onClick={switchCamera}
                                    className="rounded bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                                >
                                    Switch Camera
                                </button>
                                <button
                                    onClick={toggleVideo}
                                    className="rounded bg-green-600 px-4 py-2 text-white hover:bg-green-700"
                                >
                                    {videoEnabled
                                        ? 'Turn Off Camera'
                                        : 'Turn On Camera'}
                                </button>
                                <button
                                    onClick={toggleScreenShare}
                                    className="rounded bg-purple-600 px-4 py-2 text-white hover:bg-purple-700"
                                >
                                    {screenSharing
                                        ? 'Stop Sharing'
                                        : 'Share Screen'}
                                </button>
                            </div>
                        </div>
                    </div>

                    {/* Video Grid */}
                    <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
                        {/* Main Video */}
                        {mainStreamManager && (
                            <div
                                className="col-span-full overflow-hidden rounded-lg bg-black lg:col-span-2"
                                style={{ height: '500px' }}
                            >
                                <UserVideoComponent
                                    streamManager={mainStreamManager}
                                />
                            </div>
                        )}

                        {/* Publisher Video */}
                        {publisher && (
                            <div
                                className="cursor-pointer overflow-hidden rounded-lg bg-black"
                                onClick={() => handleMainVideoStream(publisher)}
                                style={{ height: '300px' }}
                            >
                                <UserVideoComponent streamManager={publisher} />
                            </div>
                        )}

                        {/* Subscriber Videos */}
                        {subscribers.map((subscriber) => (
                            <div
                                key={subscriber.id}
                                className="cursor-pointer overflow-hidden rounded-lg bg-black"
                                onClick={() =>
                                    handleMainVideoStream(subscriber)
                                }
                                style={{ height: '300px' }}
                            >
                                <UserVideoComponent
                                    streamManager={subscriber}
                                />
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default OpenViduPage;
