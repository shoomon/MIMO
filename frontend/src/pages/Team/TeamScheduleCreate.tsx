import { ButtonDefault, Title } from '@/components/atoms';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { useNavigate } from 'react-router-dom';
import { ButtonToggleGroup, InputForm } from '@/components/molecules';
import { useState } from 'react';
import { createSchedule } from '@/apis/TeamAPI';

const TeamScheduleCreate = () => {
    const navigate = useNavigate();

    // 📝 입력값을 관리하는 상태
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        location: '',
        date: '',
        maxParticipants: 0,
        price: 0,
        status: 'REGULAR',
    });

    // 📝 입력값 변경 핸들러
    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
        const { id, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [id]: value,
        }));
    };

    // 📝 일정 등록 함수
    const handleSubmit = async () => {
        const teamId = 1; // TODO: 실제 팀 ID 적용
        const userId = 1; // TODO: 실제 사용자 ID 적용

        await createSchedule(
            teamId,
            userId,
            formData.title,
            formData.description,
            formData.location,
            formData.date,
            Number(formData.maxParticipants),
            Number(formData.price),
            formData.status as 'REGULAR' | 'AD_HOC',
        );

        navigate(-1); // 등록 후 뒤로가기
    };

    return (
        <section className="flex flex-col gap-2">
            <div className="py- flex min-h-[43px] items-end justify-end gap-2 self-stretch">
                <ButtonDefault
                    content="등록 취소"
                    type="fail"
                    onClick={() => navigate(-1)}
                />
                <ButtonDefault
                    content="일정 등록"
                    iconId="PlusCalendar"
                    iconType="svg"
                    onClick={handleSubmit}
                />
            </div>

            <BodyLayout_24>
                <div className="w-full">
                    <Title label="일정 등록" />
                </div>
                <div className="flex w-full items-center gap-6">
                    <ButtonToggleGroup
                        options={[
                            { value: 'REGULAR', label: '🗓️ 정기 모임' },
                            { value: 'AD_HOC', label: '⚡번개 모임' },
                        ]}
                        defaultValue="REGULAR"
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
                        onChange={handleChange}
                    />
                    <InputForm
                        id="description"
                        label="일정 소개"
                        type="text"
                        placeholder="내용을 입력하세요"
                        multiline
                        count={400}
                        onChange={handleChange}
                    />
                    <InputForm
                        id="date"
                        label="일시"
                        type="date"
                        onChange={handleChange}
                    />
                    <InputForm
                        id="location"
                        label="위치"
                        type="text"
                        placeholder="카카오맵 api 붙이기"
                        onChange={handleChange}
                    />
                    <InputForm
                        id="maxParticipants"
                        label="인원 제한"
                        type="number"
                        onChange={handleChange}
                    />
                    <InputForm
                        id="price"
                        label="참가비"
                        type="number"
                        onChange={handleChange}
                    />
                </section>
            </BodyLayout_24>
        </section>
    );
};

export default TeamScheduleCreate;
