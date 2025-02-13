import React, { useState } from 'react';
import { ButtonDefault, Title } from '@/components/atoms';
import { InputForm } from '@/components/molecules';
import { createTeam, validTeamName } from '@/apis/TeamAPI';

export interface TeamData {
    name: string;
    description: string;
    area: string;
    category: string;
    nickname: string;
    notificationStatus: string;
    teamRecruitStatus: string;
    teamPrivateStatus: string;
    maxCapacity: string;
    teamProfile?: File | null;
}

interface Step {
    field: keyof TeamData;
    label: string;
    inputType?: 'text' | 'select' | 'file';
    options?: { value: string; label: string }[];
    validator?: (value: string) => boolean;
    checkDuplicate?: boolean;
}

const steps: Step[] = [
    {
        field: 'name',
        label: '모임 제목',
        inputType: 'text',
        validator: (v) => !!v.trim(),
        checkDuplicate: true,
    },
    {
        field: 'description',
        label: '모임 내용',
        inputType: 'text',
        validator: (v) => !!v.trim(),
    },
    {
        field: 'area',
        label: '지역',
        inputType: 'select',
        options: [
            { value: 'SEOUL', label: '서울' },
            { value: 'GYEONGGI', label: '경기도' },
            { value: 'GANGWON', label: '강원도' },
            { value: 'CHUNGCHEONG_NORTH', label: '충청북도' },
            { value: 'CHUNGCHEONG_SOUTH', label: '충청남도' },
            { value: 'JEOLLA_NORTH', label: '전라북도' },
            { value: 'JEOLLA_SOUTH', label: '전라남도' },
            { value: 'GYEONGSANG_NORTH', label: '경상북도' },
            { value: 'GYEONGSANG_SOUTH', label: '경상남도' },
            { value: 'JEJU', label: '제주특별자치도' },
            { value: 'SEJONG', label: '세종특별자치시' },
        ],
        validator: (v) => !!v.trim(),
    },
    {
        field: 'category',
        label: '카테고리',
        inputType: 'select',
        options: [
            { value: 'BIKE', label: '바이크' },
            { value: 'BOOK', label: '독서' },
            { value: 'CAR', label: '자동차' },
            { value: 'COOK', label: '요리' },
            { value: 'PET', label: '반려동물' },
            { value: 'SPORTS', label: '스포츠' },
            { value: 'GAME', label: '게임' },
            { value: 'HEALTH', label: '헬스' },
            { value: 'MUSIC', label: '음악/악기' },
            { value: 'PHOTO', label: '사진/영상' },
        ],
        validator: (v) => !!v.trim(),
    },
    {
        field: 'nickname',
        label: '닉네임',
        inputType: 'text',
        validator: (v) => !!v.trim(),
    },
    {
        field: 'notificationStatus',
        label: '알림 설정',
        inputType: 'select',
        options: [
            { value: 'ACTIVE', label: '알람 수신' },
            { value: 'INACTIVE', label: '알람 수신 거부' },
        ],
        validator: (v) => !!v.trim(),
    },
    {
        field: 'teamRecruitStatus',
        label: '모집 상태',
        inputType: 'select',
        options: [
            { value: 'ACTIVE_PRIVATE', label: '모집 중(방장 승인 필요)' },
            { value: 'ACTIVE_PUBLIC', label: '모집 중(자유가입)' },
            { value: 'INACTIVE', label: '비 모집 중' },
        ],
        validator: (v) => !!v.trim(),
    },
    {
        field: 'teamPrivateStatus',
        label: '비공개 여부',
        inputType: 'select',
        options: [
            { value: 'PRIVATE', label: '공개' },
            { value: 'PUBLIC', label: '비공개' },
        ],
        validator: (v) => !!v.trim(),
    },
    {
        field: 'maxCapacity',
        label: '최대 인원',
        inputType: 'text',
        validator: (v) => !!v.trim(),
    },
    {
        field: 'teamProfile',
        label: '팀 프로필 이미지 (선택)',
        inputType: 'file',
    },
];

const TeamCreate: React.FC = () => {
    const [currentStep, setCurrentStep] = useState<number>(0);
    const [teamData, setTeamData] = useState<TeamData>({
        name: '',
        description: '',
        area: '',
        category: '',
        nickname: '',
        notificationStatus: '',
        teamRecruitStatus: '',
        teamPrivateStatus: '',
        maxCapacity: '',
        teamProfile: null,
    });
    const [error, setError] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);

    const currentField = steps[currentStep];

    // 파일 및 select 입력용 핸들러
    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
    ) => {
        if (currentField.inputType === 'file') {
            const target = e.target as HTMLInputElement;
            setTeamData((prev) => ({
                ...prev,
                [currentField.field]:
                    target.files && target.files[0] ? target.files[0] : null,
            }));
        } else if (currentField.inputType === 'select') {
            setTeamData((prev) => ({
                ...prev,
                [currentField.field]: e.target.value,
            }));
        }
    };

    // 텍스트 입력 전용 핸들러 (InputForm에서 사용)
    const handleTextChange: React.ChangeEventHandler<
        HTMLInputElement | HTMLTextAreaElement
    > = (e) => {
        setTeamData((prev) => ({
            ...prev,
            [currentField.field]: e.target.value,
        }));
    };

    const handleNext = async () => {
        setError('');
        const value = teamData[currentField.field];
        if (
            currentField.validator &&
            typeof value === 'string' &&
            !currentField.validator(value)
        ) {
            setError(`${currentField.label}를 입력해주세요.`);
            return;
        }

        // 제목 중복 체크
        if (currentField.checkDuplicate && typeof value === 'string') {
            setLoading(true);
            try {
                const isAvailable = await validTeamName(value);
                if (isAvailable) {
                    setError('이미 사용 중인 모임 제목입니다.');
                    setLoading(false);
                    return;
                }
            } catch (err) {
                console.log(err);
                setError('제목 중복 체크 중 오류가 발생했습니다.');
                setLoading(false);
                return;
            }
            setLoading(false);
        }

        if (currentStep < steps.length - 1) {
            setCurrentStep((prev) => prev + 1);
        } else {
            await submitForm();
        }
    };

    const handlePrev = () => {
        if (currentStep > 0) {
            setError('');
            setCurrentStep((prev) => prev - 1);
        }
    };

    const submitForm = async () => {
        setLoading(true);
        setError('');
        try {
            // plain object를 전달하고, API 내부에서 FormData로 변환
            await createTeam(teamData);
            alert('팀 생성이 완료되었습니다.');
        } catch (err) {
            console.log(err);

            setError('팀 생성 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    const renderInput = () => {
        if (currentField.inputType === 'select') {
            return (
                <div>
                    <label htmlFor={currentField.field}>
                        {currentField.label}
                    </label>
                    <select
                        id={currentField.field}
                        value={teamData[currentField.field] as string}
                        onChange={handleChange}
                        className="input-class"
                    >
                        <option value="">선택해주세요</option>
                        {currentField.options?.map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
            );
        } else if (currentField.inputType === 'file') {
            return (
                <div>
                    <label htmlFor={currentField.field}>
                        {currentField.label}
                    </label>
                    <input
                        type="file"
                        id={currentField.field}
                        accept="image/jpeg"
                        onChange={handleChange}
                    />
                </div>
            );
        } else {
            // 텍스트 입력은 InputForm 사용 (handleTextChange는 union 타입으로 정의됨)
            return (
                <InputForm
                    id={currentField.field}
                    label={currentField.label}
                    value={teamData[currentField.field] as string}
                    onChange={handleTextChange}
                />
            );
        }
    };

    return (
        <div className="flex justify-center">
            <div className="center flex w-[800px] flex-col items-center justify-center">
                <div className="mt-12 mb-24 flex w-full border-b border-gray-300 pb-4">
                    <Title label="모임 생성" />
                </div>
                {renderInput()}
                {error && <div className="text-fail mt-2">{error}</div>}
                <div className="mt-4 flex space-x-2">
                    {currentStep > 0 && (
                        <ButtonDefault
                            onClick={handlePrev}
                            disabled={loading}
                            type="default"
                            content="이전"
                        />
                    )}
                    <ButtonDefault
                        onClick={handleNext}
                        disabled={loading}
                        type="primary"
                        content={
                            currentStep === steps.length - 1 ? '제출' : '다음'
                        }
                    />
                </div>
            </div>
        </div>
    );
};

export default TeamCreate;
