import React, { useState, useEffect, useRef } from 'react';
import { useQuery } from '@tanstack/react-query';
import { ButtonDefault, Title } from '@/components/atoms';
import { InputForm } from '@/components/molecules';
import {
    createTeam,
    validTeamName,
    getCategory,
    getArea,
} from '@/apis/TeamAPI';
import { TagsResponse, TeamCreateRequest } from '@/types/Team';
import { useNavigate } from 'react-router-dom';

interface Step {
    field: keyof TeamCreateRequest;
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
        label: '모임 설명',
        inputType: 'text',
        validator: (v) => !!v.trim(),
    },
    {
        field: 'area',
        label: '지역',
        inputType: 'select',
        validator: (v) => !!v.trim(),
    },
    {
        field: 'category',
        label: '카테고리',
        inputType: 'select',
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
            { value: 'ACTIVE_PRIVATE', label: '모임장 승인 필요' },
            { value: 'ACTIVE_PUBLIC', label: '자유가입' },
            { value: 'INACTIVE', label: '비 모집' },
        ],
        validator: (v) => !!v.trim(),
    },
    {
        field: 'teamPrivateStatus',
        label: '비공개 여부',
        inputType: 'select',
        options: [
            { value: 'PUBLIC', label: '공개' },
            { value: 'PRIVATE', label: '비공개' },
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
    // 지금까지 방문한 가장 큰 스텝 인덱스 (마지막 스텝까지 방문했는지 체크하기 위함)
    const [maxStep, setMaxStep] = useState<number>(0);

    const [teamData, setTeamData] = useState<TeamCreateRequest>({
        name: '',
        description: '',
        area: '',
        category: '',
        nickname: '',
        notificationStatus: 'ACTIVE',
        teamRecruitStatus: 'ACTIVE_PUBLIC',
        teamPrivateStatus: 'PUBLIC',
        maxCapacity: '10',
        teamProfile: null,
    });
    const [error, setError] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);
    const navigate = useNavigate();

    const { data: categoryData } = useQuery<TagsResponse>({
        queryKey: ['categoryList'],
        queryFn: () => getCategory(),
    });
    const { data: areaData } = useQuery<TagsResponse>({
        queryKey: ['areaList'],
        queryFn: () => getArea(),
    });

    // 프로필 이미지 미리보기를 위한 상태
    const [previewUrl, setPreviewUrl] = useState<string>('');
    useEffect(() => {
        if (teamData.teamProfile) {
            const objectUrl = URL.createObjectURL(teamData.teamProfile as File);
            setPreviewUrl(objectUrl);
            return () => URL.revokeObjectURL(objectUrl);
        } else {
            setPreviewUrl('');
        }
    }, [teamData.teamProfile]);

    // 새 문항이 추가되었을 때 자동 스크롤
    const bottomRef = useRef<HTMLDivElement>(null);
    useEffect(() => {
        if (bottomRef.current) {
            bottomRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [maxStep]);

    const currentField = steps[currentStep];

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

    const handleTextChange: React.ChangeEventHandler<
        HTMLInputElement | HTMLTextAreaElement
    > = (e) => {
        let { value } = e.target;

        if (currentField.field === 'maxCapacity') {
            // 숫자만 입력 허용
            value = value.replace(/[^0-9]/g, '');

            const numericValue = Number(value);

            if (numericValue < 1) {
                setError('최대 인원은 최소 1명 이상이어야 합니다.');
                value = '1';
            } else if (numericValue > 1000) {
                setError('최대 인원은 1000명을 초과할 수 없습니다.');
                value = '1000';
            } else {
                setError(''); // 정상 입력 시 에러 초기화
            }
        }
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

        if (currentField.checkDuplicate && typeof value === 'string') {
            // 한글, 영문, 숫자, 공백, 이모지만 허용
            const isValidFormat =
                /^[\p{Script=Hangul}a-zA-Z0-9\s\p{Emoji}]+$/u.test(value);

            // 한글, 영문, 숫자가 하나라도 포함되어야 함 (이모지만 있는 것도 허용)
            const containsValidChar = /[가-힣a-zA-Z0-9]/.test(value);

            // 문자열 내에 한글 자음(ㄱ-ㅎ)이 포함되어 있으면 거부
            const containsConsonants = /[ㄱ-ㅎ]/.test(value);

            if (
                !isValidFormat ||
                (!containsValidChar && !/\p{Emoji}/u.test(value)) ||
                containsConsonants
            ) {
                setError(
                    '모임 제목에는 한글, 영문, 숫자 또는 이모지만 사용할 수 있으며, 한글 자음(ㄱ-ㅎ)은 포함될 수 없습니다.',
                );
                return;
            }

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

        // 아직 마지막 스텝이 아니라면 다음 스텝으로 이동
        if (currentStep < steps.length - 1) {
            const nextStep = currentStep + 1;
            setCurrentStep(nextStep);
            setMaxStep((prev) => Math.max(prev, nextStep));
        } else {
            // 마지막 스텝이면 폼 제출
            await submitForm();
        }
    };

    const submitForm = async () => {
        setLoading(true);
        setError('');
        try {
            await createTeam(teamData);
            alert('팀 생성이 완료되었습니다.');
            navigate('/');
        } catch (err) {
            console.log(err);
            setError('팀 생성 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    /**
     * 이전 스텝일 때 표시될 텍스트 (value -> label로 치환)
     */
    const getDisplayValue = (step: Step, value: string | File | null) => {
        if (step.inputType === 'file') {
            // 파일일 경우 미리보기가 있으면 이미지 표시, 없으면 텍스트
            return value ? '이미지 업로드됨' : '파일 미선택';
        }
        if (typeof value === 'string') {
            let dynamicOptions: { value: string; label: string }[] = [];

            if (step.field === 'area' && areaData) {
                dynamicOptions = areaData.tags.map((tag) => ({
                    value: tag.name,
                    label: tag.name,
                }));
            } else if (step.field === 'category' && categoryData) {
                dynamicOptions = categoryData.tags.map((tag) => ({
                    value: tag.name,
                    label: tag.name,
                }));
            } else if (step.options) {
                dynamicOptions = step.options;
            }

            if (dynamicOptions.length > 0) {
                const found = dynamicOptions.find((opt) => opt.value === value);
                return found ? found.label : value;
            }
            return value.trim() ? value : '미입력';
        }
        return '미입력';
    };

    /**
     * 스텝 렌더링
     */
    const renderStep = (step: Step, index: number) => {
        const isActive = index === currentStep;
        const value = teamData[step.field];
        let content;

        if (isActive) {
            // 활성 스텝
            if (step.inputType === 'select') {
                let options = step.options || [];
                if (step.field === 'area' && areaData) {
                    options = areaData.tags.map((tag) => ({
                        value: tag.name,
                        label: tag.name,
                    }));
                } else if (step.field === 'category' && categoryData) {
                    options = categoryData.tags.map((tag) => ({
                        value: tag.name,
                        label: tag.name,
                    }));
                }
                content = (
                    <select
                        id={step.field}
                        value={value as string}
                        onChange={handleChange}
                        className="input-class"
                    >
                        <option value="">선택해주세요</option>
                        {options.map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                );
            } else if (step.inputType === 'file') {
                content = (
                    <>
                        <input
                            type="file"
                            id={step.field}
                            accept="image/jpeg, image/png, image/webp"
                            onChange={handleChange}
                        />
                        {previewUrl && (
                            <div className="mt-2">
                                <img
                                    src={previewUrl}
                                    alt="프로필 이미지 미리보기"
                                    className="h-32 w-32 object-cover"
                                />
                            </div>
                        )}
                    </>
                );
            } else {
                // text 타입
                content = (
                    <InputForm
                        id={step.field}
                        value={value as string}
                        onChange={handleTextChange}
                    />
                );
            }
        } else {
            // 이전(또는 이미 넘어간) 스텝: 읽기 전용 + 클릭 시 해당 스텝으로 이동
            if (step.inputType === 'file') {
                content = (
                    <div
                        className="cursor-pointer"
                        onClick={() => setCurrentStep(index)}
                    >
                        {previewUrl ? (
                            <img
                                src={previewUrl}
                                alt="프로필 이미지 미리보기"
                                className="h-32 w-32 object-cover"
                            />
                        ) : (
                            <p>파일 미선택</p>
                        )}
                    </div>
                );
            } else {
                const displayValue = getDisplayValue(step, value!);
                content = (
                    <div
                        className="cursor-pointer"
                        onClick={() => setCurrentStep(index)}
                    >
                        <p>{displayValue}</p>
                    </div>
                );
            }
        }

        return (
            <div
                key={step.field}
                className="mb-4 rounded border-b border-gray-300 p-3"
            >
                <label
                    htmlFor={step.field}
                    className="mb-1 block font-semibold"
                >
                    {step.label}
                </label>
                {content}
            </div>
        );
    };

    // 한 번이라도 마지막 스텝에 도달했다면 버튼 라벨을 '제출'로 고정
    const buttonLabel = maxStep === steps.length - 1 ? '제출' : '다음';

    return (
        <div className="flex justify-center">
            <div className="center mb-20 flex w-full flex-col items-center justify-center">
                <div className="mt-12 mb-8 w-full border-b border-gray-300 pb-4">
                    <Title label="모임 생성" />
                </div>
                <div className="w-full max-w-[800px]">
                    {steps
                        .slice(0, maxStep + 1)
                        .map((step, index) => renderStep(step, index))}
                    {/* 새로 추가된 스텝으로 스크롤하기 위한 dummy div */}
                    <div ref={bottomRef} />
                </div>
                {error && <div className="text-fail mt-2">{error}</div>}
                <div className="mt-4">
                    <ButtonDefault
                        onClick={handleNext}
                        disabled={loading}
                        type="primary"
                        content={buttonLabel}
                    />
                </div>
            </div>
        </div>
    );
};

export default TeamCreate;
