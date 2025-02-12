import { ButtonDefault, Title } from '@/components/atoms';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { useNavigate, useParams } from 'react-router-dom';
import { ButtonToggleGroup, InputForm } from '@/components/molecules';
import { useEffect, useState } from 'react';
import { getSpecificSchedule, updateSchedule } from '@/apis/TeamAPI';

const TeamScheduleEdit = () => {
    const navigate = useNavigate();
    const { teamId, scheduleId } = useParams<{
        teamId: string;
        scheduleId: string;
    }>();

    // formData 상태 (편집 전 기존 일정 데이터로 초기화될 예정)
    const [formData, setFormData] = useState({
        teamScheduleId: 0, // 추가: 백엔드에서 받아온 teamScheduleId를 저장합니다.
        title: '',
        description: '',
        location: '',
        date: '',
        maxParticipants: 0,
        price: 0,
        status: 'REGULAR',
    });

    // 일정 상세 정보를 불러와 formData를 초기화
    useEffect(() => {
        if (teamId && scheduleId) {
            getSpecificSchedule(Number(teamId), Number(scheduleId))
                .then((data) => {
                    setFormData({
                        teamScheduleId: data.teamScheduleId, // 받아온 teamScheduleId
                        title: data.title,
                        description: data.description,
                        location: data.location,
                        // API에서 받은 date가 Date 타입이거나 ISO 문자열이라면
                        // "YYYY-MM-DDTHH:MM" 형식으로 변환
                        date: new Date(data.date).toISOString().slice(0, 16),
                        maxParticipants: data.maxParticipants,
                        price: data.price,
                        status: data.status, // 'REGULAR' 또는 'AD_HOC'
                    });
                })
                .catch((error) => {
                    console.error('일정 정보를 불러오지 못했습니다.', error);
                });
        }
    }, [teamId, scheduleId]);

    // 입력값 변경 핸들러
    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
        const { id, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [id]: value,
        }));
    };

    // 일정 수정 함수
    const handleSubmit = async () => {
        try {
            await updateSchedule(
                Number(teamId),
                formData.teamScheduleId,
                formData.title,
                formData.description,
                formData.location,
                formData.date,
                Number(formData.maxParticipants),
                Number(formData.price),
                formData.status as 'REGULAR' | 'AD_HOC',
            );
            navigate(-1); // 수정 후 이전 페이지로 이동
        } catch (error) {
            console.error('일정 수정 실패:', error);
            alert('일정 수정에 실패했습니다.');
        }
    };

    return (
        <section className="flex flex-col gap-2">
            {/* 상단 버튼 영역 */}
            <div className="py- flex min-h-[43px] items-end justify-end gap-2 self-stretch">
                <ButtonDefault
                    content="수정 취소"
                    type="fail"
                    onClick={() => navigate(-1)}
                />
                <ButtonDefault
                    content="일정 수정"
                    iconId="PlusCalendar"
                    iconType="svg"
                    onClick={handleSubmit}
                />
            </div>

            <BodyLayout_24>
                <div className="w-full">
                    <Title label="일정 수정" />
                </div>
                <div className="flex w-full items-center gap-6">
                    <ButtonToggleGroup
                        options={[
                            { value: 'REGULAR', label: '🗓️ 정기 모임' },
                            { value: 'AD_HOC', label: '⚡번개 모임' },
                        ]}
                        defaultValue={formData.status}
                        onChange={(value) =>
                            setFormData((prev) => ({ ...prev, status: value }))
                        }
                    />
                    정기 모임은 부방장 등급 이상부터 생성할 수 있어요.
                </div>
                <section className="flex w-full flex-col gap-5">
                    <InputForm
                        id="title"
                        label="일정 제목"
                        type="text"
                        placeholder="제목을 입력하세요"
                        value={formData.title}
                        onChange={handleChange}
                    />
                    <InputForm
                        id="description"
                        label="일정 소개"
                        type="text"
                        placeholder="내용을 입력하세요"
                        multiline={true}
                        count={400}
                        value={formData.description}
                        onChange={handleChange}
                    />
                    <InputForm
                        id="date"
                        label="일시"
                        type="datetime-local"
                        value={formData.date}
                        onChange={handleChange}
                    />
                    <InputForm
                        id="location"
                        label="위치"
                        type="text"
                        placeholder="카카오맵 api 붙이기"
                        value={formData.location}
                        onChange={handleChange}
                    />
                    <InputForm
                        id="maxParticipants"
                        label="인원 제한"
                        type="number"
                        value={formData.maxParticipants}
                        onChange={handleChange}
                    />
                    <InputForm
                        id="price"
                        label="참가비"
                        type="number"
                        value={formData.price}
                        onChange={handleChange}
                    />
                </section>
            </BodyLayout_24>
        </section>
    );
};

export default TeamScheduleEdit;
