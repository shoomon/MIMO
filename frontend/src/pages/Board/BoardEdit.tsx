// pages/BoardEdit.tsx
import { updateBoard, getBoardDetail } from '@/apis/TeamBoardAPI';
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ButtonDefault, Title } from '@/components/atoms';
import BaseLayout from '../layouts/BaseLayout';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import { InputForm } from '@/components/molecules';
import ButtonLayout from '../layouts/ButtonLayout';

interface BoardFile {
    fileId: number;
    fileUri: string;
    fileExtension: string;
}

interface ExistingFile {
    id: string; // fileId (string 변환)
    url: string; // fileUri
    extension: string; // fileExtension
}

const BoardEdit = () => {
    const { teamId, teamBoardId, postId } = useParams<{
        teamId: string;
        teamBoardId: string;
        postId: string;
    }>();
    const navigate = useNavigate();

    // (1) 제목/설명
    const [formData, setFormData] = useState({
        title: '',
        description: '',
    });

    // (2) 기존 파일 목록 + 삭제할 파일들
    const [existingFiles, setExistingFiles] = useState<ExistingFile[]>([]);
    const [filesToDelete, setFilesToDelete] = useState<string[]>([]);

    // (3) 새로 추가된 파일들
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const [filePreviews, setFilePreviews] = useState<string[]>([]);

    // (4) 로딩/에러
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    // ─────────────────────────────────────────────────────────
    // A) 기존 게시글 상세 로드
    // ─────────────────────────────────────────────────────────
    useEffect(() => {
        if (!postId) return;

        getBoardDetail(postId)
            .then((data) => {
                // 예: data.board.postTitle, data.board.description
                setFormData({
                    title: data.board.postTitle,
                    description: data.board.description,
                });

                if (data.files) {
                    setExistingFiles(
                        data.files.map((file: BoardFile) => ({
                            id: String(file.fileId),
                            url: file.fileUri,
                            extension: file.fileExtension,
                        })),
                    );
                }
            })
            .catch((err) => {
                console.error(err);
                setError('게시글 정보를 불러오는데 실패했습니다.');
            });
    }, [postId]);

    // ─────────────────────────────────────────────────────────
    // B) 새 파일 선택
    // ─────────────────────────────────────────────────────────
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setError('');
        if (e.target.files) {
            const filesArray = Array.from(e.target.files);
            const newValidFiles: File[] = [];
            const newPreviews: string[] = [];

            // 단일 파일 5MB 제한
            filesArray.forEach((file) => {
                if (file.size > 5 * 1024 * 1024) {
                    setError(`파일 ${file.name}의 용량이 5MB를 초과합니다.`);
                } else {
                    newValidFiles.push(file);
                    newPreviews.push(URL.createObjectURL(file));
                }
            });

            // 전체 10MB 제한
            const currentTotalSize = selectedFiles.reduce(
                (acc, file) => acc + file.size,
                0,
            );
            const newTotalSize =
                currentTotalSize +
                newValidFiles.reduce((acc, f) => acc + f.size, 0);

            if (newTotalSize > 10 * 1024 * 1024) {
                setError('전체 파일 업로드 용량이 10MB를 초과합니다.');
                return;
            }

            // 최종 추가
            setSelectedFiles((prev) => [...prev, ...newValidFiles]);
            setFilePreviews((prev) => [...prev, ...newPreviews]);
        }
    };

    // 언마운트 시 URL 정리
    useEffect(() => {
        return () => {
            filePreviews.forEach((url) => URL.revokeObjectURL(url));
        };
    }, [filePreviews]);

    // 새 파일 개별 제거
    const removeNewFile = (index: number) => {
        URL.revokeObjectURL(filePreviews[index]);
        setSelectedFiles((prev) => prev.filter((_, i) => i !== index));
        setFilePreviews((prev) => prev.filter((_, i) => i !== index));
    };

    // ─────────────────────────────────────────────────────────
    // C) 기존 파일 삭제/취소
    // ─────────────────────────────────────────────────────────
    const toggleExistingFileDeletion = (fileId: string) => {
        if (filesToDelete.includes(fileId)) {
            setFilesToDelete((prev) => prev.filter((id) => id !== fileId));
        } else {
            setFilesToDelete((prev) => [...prev, fileId]);
        }
    };

    // ─────────────────────────────────────────────────────────
    // D) 폼 제출 (게시글 수정)
    // ─────────────────────────────────────────────────────────
    const handleSubmit = async (e?: React.FormEvent) => {
        e?.preventDefault();

        if (!teamId || !teamBoardId || !postId) {
            setError('필수 파라미터가 누락되었습니다.');
            return;
        }

        // 새 파일 전체 용량 체크
        const totalSize = selectedFiles.reduce(
            (acc, file) => acc + file.size,
            0,
        );
        if (totalSize > 10 * 1024 * 1024) {
            setError('전체 파일 업로드 용량이 10MB를 초과합니다.');
            return;
        }

        setLoading(true);
        setError('');

        try {
            // 1) 삭제할 파일들: [{ fileId, fileExtension, fileUri }, ...]
            const filesToDeleteObjects = existingFiles
                .filter((file) => filesToDelete.includes(file.id))
                .map((file) => ({
                    fileId: Number(file.id),
                    fileExtension: file.extension,
                    fileUri: file.url,
                }));

            // 2) updateBoard 호출
            //    - filesToDeleteObjects는 JSON.stringify
            //    - 새 파일들은 selectedFiles 그대로
            await updateBoard(
                postId,
                formData.title,
                formData.description,
                JSON.stringify(filesToDeleteObjects),
                selectedFiles,
            );

            // 수정 성공
            navigate(-1);
        } catch (err) {
            console.error('Error updating board:', err);
            setError('게시글 수정 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    // 새 파일 용량 (MB)
    const totalSizeMB = (
        selectedFiles.reduce((acc, file) => acc + file.size, 0) /
        (1024 * 1024)
    ).toFixed(2);

    // ─────────────────────────────────────────────────────────
    // E) 렌더링
    // ─────────────────────────────────────────────────────────
    return (
        <BaseLayout>
            <ButtonLayout>
                <ButtonDefault
                    type="primary"
                    content="수정하기"
                    disabled={loading}
                    onClick={handleSubmit}
                />
            </ButtonLayout>

            <BodyLayout_64>
                <div className="flex w-full border-b border-gray-300 pb-1">
                    <Title label="글 수정" />
                </div>

                {error && <div className="text-fail mb-4">{error}</div>}

                <form onSubmit={handleSubmit} className="w-full space-y-4">
                    {/* 제목 */}
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

                    {/* 설명 */}
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

                    {/* 파일 첨부 (새 파일) */}
                    <div>
                        <label className="flex flex-col gap-2">
                            <div className="w-fit rounded-sm border px-2 py-1">
                                파일 첨부
                            </div>
                            <input
                                type="file"
                                multiple
                                onChange={handleFileChange}
                                className="input-class w-full"
                            />
                            <div className="mt-1 text-sm text-gray-500">
                                파일 당 최대 5MB, 전체 10MB까지 업로드
                                가능합니다.
                            </div>
                            <div className="mt-1 text-sm text-gray-500">
                                현재 총 파일 용량: {totalSizeMB} MB / 10 MB
                            </div>
                        </label>
                    </div>

                    {/* 기존 첨부 파일 목록 */}
                    {existingFiles.length > 0 && (
                        <div className="mt-4">
                            <div className="mb-2 text-sm font-medium text-gray-700">
                                기존 첨부 파일
                            </div>
                            <div className="grid grid-cols-3 gap-4">
                                {existingFiles.map((file) => (
                                    <div key={file.id} className="relative">
                                        <img
                                            src={file.url}
                                            alt="existing file"
                                            className="h-32 w-full rounded object-cover"
                                        />
                                        <button
                                            type="button"
                                            onClick={() =>
                                                toggleExistingFileDeletion(
                                                    file.id,
                                                )
                                            }
                                            className={`absolute top-1 right-1 rounded px-2 pt-1 text-xs font-light text-white ${
                                                filesToDelete.includes(file.id)
                                                    ? 'bg-green-500'
                                                    : 'bg-opacity-50 bg-red-500'
                                            }`}
                                        >
                                            {filesToDelete.includes(file.id)
                                                ? '취소'
                                                : '삭제'}
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* 새로 추가된 파일 미리보기 */}
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
                                        onClick={() => removeNewFile(index)}
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

export default BoardEdit;
