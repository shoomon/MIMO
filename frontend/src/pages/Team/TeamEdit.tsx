import { useState, useEffect } from 'react';
import { getTeamInfo } from '@/apis/TeamAPI';
import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import { TeamInfoResponse } from '@/types/Team';

const TeamEdit = () => {
    const { teamId } = useParams<{ teamId: string }>();

    // API로부터 팀 정보를 가져옵니다.
    const {
        data: teamData,
        isLoading,
        error,
    } = useQuery<TeamInfoResponse>({
        queryKey: ['teamInfo', teamId],
        queryFn: () => getTeamInfo(teamId!),
        staleTime: 1000 * 60 * 5, // 5분 동안 데이터 신선하게 유지
    });

    // API로 받아온 팀 정보를 수정할 수 있도록 state로 관리합니다.
    const [editedTeam, setEditedTeam] = useState<TeamInfoResponse | null>(null);

    // 팀 정보가 받아지면 local state에 초기화합니다.
    useEffect(() => {
        if (teamData) {
            setEditedTeam(teamData);
        }
    }, [teamData]);

    // 입력값 변경 핸들러 (예: name과 description 필드를 수정하는 경우)
    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
        const { name, value } = e.target;
        setEditedTeam((prev) => (prev ? { ...prev, [name]: value } : prev));
    };

    if (isLoading) return <div>로딩 중...</div>;
    if (error) return <div>오류가 발생했습니다.</div>;

    return (
        <div>
            <h1>팀 정보 수정</h1>
            {editedTeam && (
                <form>
                    {/* 예시로 팀 이름과 설명 수정 */}
                    <div>
                        <label htmlFor="name">팀 이름:</label>
                        <input
                            id="name"
                            name="name"
                            type="text"
                            value={editedTeam.name}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label htmlFor="description">팀 설명:</label>
                        <textarea
                            id="description"
                            name="description"
                            value={editedTeam.description}
                            onChange={handleChange}
                        />
                    </div>
                    {/* 기타 필요한 필드들을 추가 */}
                    <button type="submit">저장</button>
                </form>
            )}
        </div>
    );
};

export default TeamEdit;
