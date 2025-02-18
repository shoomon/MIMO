import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
    getArea,
    getCategory,
    getTeamSpecificInfo,
    updateTeam,
} from '@/apis/TeamAPI';
import { TeamPrivateStatus, TeamRecruitStatus } from '@/types/Team';

import BaseLayout from '../layouts/BaseLayout';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import ButtonLayout from '../layouts/ButtonLayout';
import { ButtonDefault, Title } from '@/components/atoms';
import { InputForm, ButtonToggleGroup } from '@/components/molecules';

const TeamEdit = () => {
    const { teamId } = useParams<{ teamId: string }>();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    // 팀 사진 파일과 미리보기 상태
    const [profileFile, setProfileFile] = useState<File | null>(null);
    const [profilePreview, setProfilePreview] = useState<string>('');

    // 팀 정보 로딩 (팀 ID가 있을 때만 실행)
    const { data: teamData, isLoading } = useQuery({
        queryKey: ['teamInfo', teamId],
        queryFn: () => getTeamSpecificInfo(teamId!),
        enabled: !!teamId,
    });

    // 카테고리 목록 로딩
    const { data: catgoryList } = useQuery({
        queryKey: ['catgoryList', teamId],
        queryFn: () => getCategory(),
    });

    // 지역 목록 로딩
    const { data: areaList } = useQuery({
        queryKey: ['areaList', teamId],
        queryFn: () => getArea(),
    });

    // 수정할 폼 데이터 상태
    const [formData, setFormData] = useState<{
        name: string;
        description: string;
        recruitStatus: TeamRecruitStatus;
        privateStatus: TeamPrivateStatus;
        area: string;
        category: string;
        profileUri: string;
    }>({
        name: '',
        description: '',
        recruitStatus: 'ACTIVE_PUBLIC' as TeamRecruitStatus,
        privateStatus: 'PRIVATE' as TeamPrivateStatus,
        area: '',
        category: '',
        profileUri: '',
    });

    // API에서 받아온 팀 정보를 폼 데이터에 초기화
    useEffect(() => {
        if (teamData) {
            setFormData({
                profileUri: teamData.profileUri || '',
                name: teamData.name,
                description: teamData.description,
                recruitStatus: teamData.recruitStatus,
                privateStatus: teamData.privateStatus,
                area: teamData.area,
                category: teamData.category,
            });
            // 기존 팀 사진 미리보기 설정 (이 값은 화면 렌더링 전용)
            setProfilePreview(teamData.profileUri || '');
        }
    }, [teamData]);

    // updateTeam API 호출을 위한 mutation (profile은 새 파일이 있을 때만 File 객체 전달)
    const mutation = useMutation<
        boolean,
        Error,
        {
            teamId: string;
            name: string;
            description: string;
            recruitStatus: TeamRecruitStatus;
            privateStatus: TeamPrivateStatus;
            area: string;
            category: string;
            profile?: File;
        }
    >({
        mutationFn: ({
            teamId,
            name,
            description,
            recruitStatus,
            privateStatus,
            area,
            category,
            profile,
        }) =>
            updateTeam(
                teamId,
                name,
                description,
                recruitStatus,
                privateStatus,
                area,
                category,
                profile,
            ),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['teamInfo', teamId] });
            navigate(-1);
        },
        onError: () => {
            setError('팀 정보 수정 중 오류가 발생했습니다.');
        },
        onSettled: () => {
            setLoading(false);
        },
    });

    // 입력값 변경 핸들러 (Input, TextArea, Select 모두 사용 가능)
    const handleChange = (
        e: React.ChangeEvent<
            HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
        >,
    ) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    // 팀 공개여부 토글 변경 핸들러
    const handleToggleChange = (value: string) => {
        setFormData((prev) => ({
            ...prev,
            privateStatus: value as TeamPrivateStatus,
        }));
    };

    // 가입 방식 토글 변경 핸들러
    const handleRecruitToggleChange = (value: string) => {
        setFormData((prev) => ({
            ...prev,
            recruitStatus: value as TeamRecruitStatus,
        }));
    };

    // 팀 사진 파일 변경 핸들러
    const handleProfileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const file = e.target.files[0];
            setProfileFile(file);
            const previewUrl = URL.createObjectURL(file);
            setProfilePreview(previewUrl);
            // 이 previewUrl은 화면에 보여주기 위해서만 사용합니다.
            // updateTeam API 호출 시에는 새 File 객체(profileFile)만 전송합니다.
        }
    };

    // 폼 제출 핸들러
    const handleSubmit = (
        e: React.FormEvent<HTMLFormElement> | React.MouseEvent,
    ) => {
        e.preventDefault();
        if (!teamId) {
            setError('팀 ID가 누락되었습니다.');
            return;
        }
        setLoading(true);
        setError('');
        mutation.mutate({
            teamId,
            name: formData.name,
            description: formData.description,
            recruitStatus: formData.recruitStatus,
            privateStatus: formData.privateStatus,
            area: formData.area,
            category: formData.category,
            // 새 파일이 선택된 경우에만 profile 필드를 전달합니다.
            profile: profileFile || undefined,
        });
    };

    // 컴포넌트 언마운트 시 생성된 Object URL 해제
    useEffect(() => {
        return () => {
            if (profilePreview.startsWith('blob:')) {
                URL.revokeObjectURL(profilePreview);
            }
        };
    }, [profilePreview]);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    return (
        <BaseLayout>
            <ButtonLayout>
                <ButtonDefault
                    type="primary"
                    content="저장"
                    disabled={loading}
                    onClick={handleSubmit}
                />
            </ButtonLayout>

            <BodyLayout_64>
                <div className="flex w-full border-b border-gray-300 pb-1">
                    <Title label="팀 정보 수정" />
                </div>

                {error && <div className="text-fail mb-4">{error}</div>}

                <form
                    onSubmit={handleSubmit}
                    className="flex w-full flex-col gap-4 space-y-4"
                >
                    {/* 팀 사진 섹션 */}
                    <div className="flex flex-col gap-2 pl-2">
                        <span className="text-xl font-bold">팀 프로필</span>
                        {profilePreview ? (
                            <img
                                src={profilePreview}
                                alt="팀 사진"
                                className="h-32 w-32 rounded object-cover"
                            />
                        ) : (
                            <div className="flex h-32 w-32 items-center justify-center rounded bg-gray-200">
                                사진 없음
                            </div>
                        )}
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handleProfileChange}
                        />
                    </div>

                    {/* 팀 이름 입력 */}
                    <div>
                        <InputForm
                            id="name"
                            type="text"
                            label="팀 이름"
                            value={formData.name}
                            onChange={handleChange}
                            placeholder="팀 이름을 입력하세요"
                        />
                    </div>

                    {/* 팀 설명 입력 */}
                    <div>
                        <InputForm
                            id="description"
                            multiline
                            label="팀 설명"
                            value={formData.description}
                            onChange={handleChange}
                            placeholder="팀 설명을 입력하세요"
                        />
                    </div>

                    {/* 팀 공개여부 설정 */}
                    <div className="flex flex-col gap-2 pb-4 pl-2">
                        <span className="text-xl font-bold">팀 공개여부</span>
                        <ButtonToggleGroup
                            options={[
                                { value: 'PUBLIC', label: '공개' },
                                { value: 'PRIVATE', label: '비공개' },
                            ]}
                            defaultValue={formData.privateStatus}
                            onChange={handleToggleChange}
                        />
                    </div>

                    {/* 가입 방식 설정 */}
                    <div className="flex flex-col gap-2 pb-4 pl-2">
                        <span className="text-xl font-bold">
                            가입 방식 설정
                        </span>
                        <ButtonToggleGroup
                            options={[
                                { value: 'ACTIVE_PUBLIC', label: '바로 가입' },
                                { value: 'ACTIVE_PRIVATE', label: '승인 필요' },
                                { value: 'INACTIVE', label: '가입 제한' },
                            ]}
                            defaultValue={formData.recruitStatus}
                            onChange={handleRecruitToggleChange}
                        />
                    </div>

                    {/* 카테고리 선택 */}
                    <div className="flex flex-col gap-2 pb-4 pl-2">
                        <span className="text-xl font-bold">카테고리 선택</span>
                        <select
                            name="category"
                            value={formData.category}
                            onChange={handleChange}
                            className="rounded border border-gray-300 p-2"
                        >
                            <option value="">카테고리 선택</option>
                            {catgoryList?.tags &&
                                catgoryList.tags.map(
                                    (tag: { tagId: number; name: string }) => (
                                        <option
                                            key={tag.tagId}
                                            value={tag.name}
                                        >
                                            {tag.name}
                                        </option>
                                    ),
                                )}
                        </select>
                    </div>

                    {/* 지역 선택 */}
                    <div className="flex flex-col gap-2 pb-4 pl-2">
                        <span className="text-xl font-bold">지역 선택</span>
                        <select
                            name="area"
                            value={formData.area}
                            onChange={handleChange}
                            className="rounded border border-gray-300 p-2"
                        >
                            <option value="">지역선택</option>
                            {areaList?.tags &&
                                areaList.tags.map(
                                    (tag: { tagId: number; name: string }) => (
                                        <option
                                            key={tag.tagId}
                                            value={tag.name}
                                        >
                                            {tag.name}
                                        </option>
                                    ),
                                )}
                        </select>
                    </div>
                </form>
            </BodyLayout_64>
        </BaseLayout>
    );
};

export default TeamEdit;
