import { ButtonDefault, Title } from '@/components/atoms';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { useNavigate, useParams } from 'react-router-dom';
import { ButtonToggleGroup, InputForm } from '@/components/molecules';
import { useState, useEffect } from 'react';
import { createSchedule } from '@/apis/TeamAPI';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

const TeamScheduleCreate = () => {
    const navigate = useNavigate();
    const { teamId } = useParams<{ teamId: string }>();
    const { data: myProfileData } = useMyTeamProfile(teamId);

    // 입력값과 에러를 관리하는 상태
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        location: '',
        date: '',
        maxParticipants: 1, // 기본 1명으로 설정
        price: 0,
        status: 'REGULAR',
    });

    const [fieldErrors, setFieldErrors] = useState<{
        title?: string;
        description?: string;
        date?: string;
        location?: string;
        maxParticipants?: string;
        price?: string;
    }>({});

    // myProfileData에 따라 초기 상태 업데이트 (LEADER가 아닌 경우 "AD_HOC" 설정)
    useEffect(() => {
        if (myProfileData && myProfileData.role !== 'LEADER') {
            setFormData((prev) => ({
                ...prev,
                status: 'AD_HOC',
            }));
        }
    }, [myProfileData]);

    // 입력값 변경 핸들러
    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
        const { id, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [id]: value,
        }));

        // 입력이 있을 때 해당 필드 에러 제거
        setFieldErrors((prev) => ({ ...prev, [id]: '' }));
    };

    // 일정 등록 함수
    const handleSubmit = async (
        e: React.FormEvent<HTMLFormElement> | React.MouseEvent,
    ) => {
        e.preventDefault();

        // 각 필드 검증
        const errors: {
            title?: string;
            description?: string;
            date?: string;
            location?: string;
            maxParticipants?: string;
            price?: string;
        } = {};

        if (!formData.title.trim()) {
            errors.title = '제목을 입력하세요.';
        }
        if (!formData.description.trim()) {
            errors.description = '일정 소개를 입력하세요.';
        }
        if (!formData.date) {
            errors.date = '일시를 입력하세요.';
        } else {
            const selectedDate = new Date(formData.date);
            const now = new Date();
            if (selectedDate <= now) {
                errors.date = '이미 지난 시간입니다..';
            }
        }
        if (!formData.location.trim()) {
            errors.location = '위치를 입력해주세요.';
        }
        if (!formData.maxParticipants || Number(formData.maxParticipants) < 1) {
            errors.maxParticipants = '인원은 최소 1명 이상이어야 합니다.';
        }
        if (Number(formData.price) < 0) {
            errors.price = '참가비는 마이너스일 수 없습니다.';
        }

        if (Object.keys(errors).length > 0) {
            setFieldErrors(errors);
            return;
        }

        // 검증 통과 시 일정 등록 API 호출
        try {
            await createSchedule(
                Number(teamId),
                formData.title,
                formData.description,
                formData.location,
                formData.date,
                Number(formData.maxParticipants),
                Number(formData.price),
                formData.status as 'REGULAR' | 'AD_HOC',
            );
            navigate(-1); // 등록 성공 시 뒤로가기
        } catch (error) {
            // 실패 시 뒤로가지 않고, 필요시 에러 처리
            console.error('일정 등록 실패:', error);
        }
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
                    {myProfileData?.role === 'LEADER' ? (
                        <>
                            <ButtonToggleGroup
                                options={[
                                    { value: 'REGULAR', label: '🗓️ 정기 모임' },
                                    { value: 'AD_HOC', label: '⚡번개 모임' },
                                ]}
                                defaultValue="REGULAR"
                                onChange={(value) =>
                                    setFormData((prev) => ({
                                        ...prev,
                                        status: value,
                                    }))
                                }
                            />
                            <span>
                                정기 모임은 부방장 등급 이상부터 생성할 수
                                있어요.
                            </span>
                        </>
                    ) : (
                        <ButtonToggleGroup
                            options={[
                                { value: 'AD_HOC', label: '⚡번개 모임' },
                            ]}
                            defaultValue="AD_HOC"
                            onChange={(value) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    status: value,
                                }))
                            }
                        />
                    )}
                </div>
                <section className="flex w-full flex-col gap-5">
                    <InputForm
                        id="title"
                        label="일정 제목"
                        type="text"
                        placeholder="제목을 입력하세요"
                        onChange={handleChange}
                        errorMessage={fieldErrors.title}
                    />
                    <InputForm
                        id="description"
                        label="일정 소개"
                        type="text"
                        placeholder="내용을 입력하세요"
                        multiline={true}
                        count={400}
                        onChange={handleChange}
                        errorMessage={fieldErrors.description}
                    />
                    <InputForm
                        id="date"
                        label="일시"
                        type="datetime-local"
                        onChange={handleChange}
                        errorMessage={fieldErrors.date}
                    />
                    <InputForm
                        id="location"
                        label="위치"
                        type="text"
                        placeholder="위치를 입력해주세요"
                        onChange={handleChange}
                        errorMessage={fieldErrors.location}
                    />
                    <InputForm
                        id="maxParticipants"
                        label="인원 제한"
                        type="number"
                        placeholder="인원 제한을 입력해주세요"
                        onChange={handleChange}
                        errorMessage={fieldErrors.maxParticipants}
                    />
                    <InputForm
                        id="price"
                        label="참가비"
                        type="number"
                        onChange={handleChange}
                        errorMessage={fieldErrors.price}
                    />
                </section>
            </BodyLayout_24>
        </section>
    );
};

export default TeamScheduleCreate;
