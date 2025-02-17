import { createBoard } from '@/apis/TeamBoardAPI';
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ButtonDefault, Title } from '@/components/atoms';
import BaseLayout from '../layouts/BaseLayout';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import { InputForm } from '@/components/molecules';
import ButtonLayout from '../layouts/ButtonLayout';

const BoardCreate = () => {
    const { teamId, teamBoardId } = useParams<{
        teamId: string;
        teamBoardId: string;
    }>();
    const navigate = useNavigate();

    // 제목과 설명 상태
    const [formData, setFormData] = useState({
        title: '',
        description: '',
    });
    // 선택된 파일들을 상태로 관리 (다중 파일 업로드, 추가 방식)
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    // 미리보기 이미지 URL 상태 (selectedFiles와 인덱스가 일치)
    const [filePreviews, setFilePreviews] = useState<string[]>([]);
    // 로딩 및 에러 상태
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    // 전체 파일 용량 (바이트 단위)
    const totalSize = selectedFiles.reduce((acc, file) => acc + file.size, 0);
    const totalSizeMB = (totalSize / (1024 * 1024)).toFixed(2);

    // 파일 input 변경 핸들러 (추가 방식)
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setError('');
        if (e.target.files) {
            const filesArray = Array.from(e.target.files);
            const newValidFiles: File[] = [];
            const newPreviews: string[] = [];

            filesArray.forEach((file) => {
                // 파일 용량 체크: 5MB = 5 * 1024 * 1024 bytes
                if (file.size > 5 * 1024 * 1024) {
                    setError(`파일 ${file.name}의 용량이 5MB를 초과합니다.`);
                } else {
                    newValidFiles.push(file);
                    newPreviews.push(URL.createObjectURL(file));
                }
            });

            // 추가 후 전체 파일 용량 체크 (10MB)
            const newTotalSize =
                totalSize +
                newValidFiles.reduce((acc, file) => acc + file.size, 0);
            if (newTotalSize > 10 * 1024 * 1024) {
                setError('전체 파일 업로드 용량이 10MB를 초과합니다.');
                return;
            }

            // 기존 파일에 추가
            setSelectedFiles((prev) => [...prev, ...newValidFiles]);
            setFilePreviews((prev) => [...prev, ...newPreviews]);
        }
    };

    // 미리보기 URL 해제 (메모리 누수 방지)
    useEffect(() => {
        return () => {
            filePreviews.forEach((url) => URL.revokeObjectURL(url));
        };
    }, [filePreviews]);

    // 개별 파일 제거 핸들러
    const removeFile = (index: number) => {
        // 미리보기 URL 해제
        URL.revokeObjectURL(filePreviews[index]);
        setSelectedFiles((prev) => prev.filter((_, i) => i !== index));
        setFilePreviews((prev) => prev.filter((_, i) => i !== index));
    };

    const handleSubmit = async (e?: React.FormEvent) => {
        if (e) {
            e.preventDefault();
        }

        // 전체 파일 용량 체크 (최종 확인)
        if (totalSize > 10 * 1024 * 1024) {
            setError('전체 파일 업로드 용량이 10MB를 초과합니다.');
            return;
        }

        if (!teamId || !teamBoardId) {
            setError('필수 파라미터가 누락되었습니다.');
            return;
        }
        setLoading(true);
        setError('');
        try {
            await createBoard({
                teamBoardId: Number(teamBoardId),
                teamId: Number(teamId),
                title: formData.title,
                description: formData.description,
                files: selectedFiles, // 선택된 파일들을 같이 전송합니다.
            });
            // 생성 성공 후 이전 페이지로 이동
            navigate(-1);
        } catch (err) {
            console.error('Error creating board:', err);
            setError('보드를 생성하는 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <BaseLayout>
            <ButtonLayout>
                <ButtonDefault
                    type="primary"
                    content="작성하기"
                    disabled={loading}
                    onClick={handleSubmit}
                />
            </ButtonLayout>
            <BodyLayout_64>
                <div className="flex w-full border-b border-gray-300 pb-1">
                    <Title label="글 작성" />
                </div>
                {error && <div className="text-fail mb-4">{error}</div>}
                <form onSubmit={handleSubmit} className="w-full space-y-4">
                    <div>
                        <InputForm
                            id="title"
                            type="text"
                            label="제목"
                            value={formData.title}
                            onChange={(e) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    title: e.target.value,
                                }))
                            }
                            placeholder="제목을 입력하세요"
                        />
                    </div>
                    <div>
                        <InputForm
                            id="desc"
                            multiline
                            label="설명"
                            value={formData.description}
                            onChange={(e) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    description: e.target.value,
                                }))
                            }
                            placeholder="설명을 입력하세요"
                        />
                    </div>
                    <div>
                        <label className="flex flex-col gap-2">
                            <div className="border- w-fit rounded-sm border px-2 py-1">
                                파일 첨부
                            </div>
                            <input
                                type="file"
                                multiple
                                onChange={handleFileChange}
                                className="input-class w-full"
                            />
                            <div className="mt-1 flex flex-col text-sm text-gray-500">
                                <span>
                                    파일 당 최대 5MB, 전체 10MB까지 업로드
                                    가능합니다.
                                </span>
                                <span>
                                    현재 총 파일 용량: {totalSizeMB} MB / 10 MB
                                </span>
                            </div>
                        </label>
                    </div>
                    {/* 업로드한 파일 미리보기 + 삭제 버튼 */}
                    {filePreviews.length > 0 && (
                        <div className="mt-4 grid grid-cols-3 gap-4">
                            {filePreviews.map((url, index) => (
                                <div key={index} className="relative">
                                    <img
                                        src={url}
                                        alt={`preview-${index}`}
                                        className="h-32 w-full rounded object-cover"
                                    />
                                    <button
                                        type="button"
                                        onClick={() => removeFile(index)}
                                        className="bg-opacity-50 bg-fail text-md absolute top-1 right-1 rounded px-2 pt-1 font-light text-white"
                                    >
                                        삭제
                                    </button>
                                    <div className="mt-1 text-xs text-gray-600">
                                        {(
                                            selectedFiles[index].size /
                                            (1024 * 1024)
                                        ).toFixed(2)}{' '}
                                        MB
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </form>
            </BodyLayout_64>
        </BaseLayout>
    );
};

export default BoardCreate;
