import { createBoard } from '@/apis/TeamBoardAPI';
import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

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
    // 선택된 파일들을 상태로 관리 (다중 파일 업로드)
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    // 로딩 및 에러 상태
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    // 파일 input 변경 핸들러
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            setSelectedFiles(Array.from(e.target.files));
        }
    };

    const handleSubmit = async () => {
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
        <div>
            <h1>보드 생성</h1>
            {error && <div style={{ color: 'red' }}>{error}</div>}
            <form
                onSubmit={(e) => {
                    e.preventDefault();
                    handleSubmit();
                }}
            >
                <div>
                    <label>
                        제목:
                        <input
                            type="text"
                            value={formData.title}
                            onChange={(e) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    title: e.target.value,
                                }))
                            }
                            placeholder="제목을 입력하세요"
                            required
                        />
                    </label>
                </div>
                <div>
                    <label>
                        설명:
                        <textarea
                            value={formData.description}
                            onChange={(e) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    description: e.target.value,
                                }))
                            }
                            placeholder="설명을 입력하세요"
                            required
                        />
                    </label>
                </div>
                <div>
                    <label>
                        파일 첨부:
                        <input
                            type="file"
                            multiple
                            onChange={handleFileChange}
                        />
                    </label>
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? '생성 중...' : '생성'}
                </button>
            </form>
        </div>
    );
};

export default BoardCreate;
