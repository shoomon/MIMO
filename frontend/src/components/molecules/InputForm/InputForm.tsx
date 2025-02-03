import { forwardRef, useState } from 'react';
import Input from '../../atoms/Input/Input';

export interface InputFormProps {
    id: string;
    label?: string;
    placeholder?: string;
    defaultValue?: string | number;
    type?: 'text' | 'email' | 'number' | 'password';
    value?: string | number;
    readOnly?: boolean;
    /** 글자수 제한 (숫자) */
    count?: number | string;
    onChange?: (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => void;
    onKeyDown?: (
        e: React.KeyboardEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => void;
    errorMessage?: string;
    disabled?: boolean;
}

/**
 * InputForm 컴포넌트는 라벨, 에러 메시지, 글자수 표시가 포함된 입력 필드를 렌더링합니다.
 *
 * 이 컴포넌트는 내부에서 입력값 상태를 관리하며, 최대 글자 수(count) 제한에 따른 유효성 검사를 수행합니다.
 * 유효성 검사 결과(글자수 초과 등)에 따라 에러 메시지를 표시하고, 해당 상태에서는 포커스 스타일을 변경합니다.
 *
 * @component
 *
 * @example
 * // 최대 20자까지 입력할 수 있는 사용자 이름 입력 필드를 렌더링합니다.
 * <InputForm
 *   id="username"
 *   label="사용자 이름"
 *   placeholder="사용자 이름을 입력하세요"
 *   count={20}
 *   onChange={(e) => console.log(e.target.value)}
 * />
 *
 * @param {Object} props - 컴포넌트의 props.
 * @param {string} props.id - 입력 요소의 고유 식별자.
 * @param {string} [props.label] - 입력 위에 표시될 라벨 텍스트.
 * @param {string} [props.placeholder] - 입력 요소의 플레이스홀더 텍스트.
 * @param {string|number} [props.defaultValue] - 입력 요소의 기본값.
 * @param {'text'|'email'|'number'|'password'} [props.type='text'] - HTML 입력 타입.
 * @param {string|number} [props.value] - 입력 요소의 제어된 값.
 * @param {boolean} [props.readOnly] - 입력 요소가 읽기 전용인지 여부.
 * @param {number|string} [props.count] - 허용되는 최대 글자 수 (유효성 검사 및 글자수 표시용).
 * @param {function} [props.onChange] - 입력값 변경 시 호출되는 콜백 함수.
 *   {@link React.ChangeEvent<HTMLInputElement|HTMLTextAreaElement>} 이벤트를 인자로 받습니다.
 * @param {function} [props.onKeyDown] - 키 입력 시 호출되는 콜백 함수.
 *   {@link React.KeyboardEvent<HTMLInputElement|HTMLTextAreaElement>} 이벤트를 인자로 받습니다.
 * @param {string} [props.errorMessage] - 외부에서 전달되는 에러 메시지 (내부 유효성 검사 에러가 우선됨).
 * @param {boolean} [props.disabled] - 입력 요소가 비활성화되었는지 여부.
 *
 * @returns {JSX.Element} 렌더링된 InputForm 컴포넌트.
 */

const InputForm = forwardRef<HTMLInputElement, InputFormProps>(
    (
        {
            id,
            label,
            placeholder,
            type = 'text',
            defaultValue,
            value,
            readOnly,
            count,
            onChange,
            onKeyDown,
            errorMessage,
            disabled,
        },
        ref,
    ) => {
        // 내부 상태: 입력값, 글자수, 내부 에러 메시지
        const [inputValue, setInputValue] = useState<string>(
            value !== undefined
                ? String(value)
                : defaultValue !== undefined
                  ? String(defaultValue)
                  : '',
        );

        const [charCount, setCharCount] = useState(inputValue.length);

        const [internalError, setInternalError] = useState<string>('');

        // 입력값 변경 시 처리 (즉시 상태 업데이트)
        const handleInputChange = (
            e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
        ) => {
            const val = e.target.value;
            setInputValue(val);
            setCharCount(val.length);

            // 글자수 초과 체크를 onChange 핸들러 내부에서 바로 수행
            if (typeof count === 'number' && val.length >= count) {
                setInternalError(`최대 ${count}자까지 입력할 수 있습니다.`);
            } else {
                setInternalError('');
            }

            if (onChange) {
                onChange(e);
            }
        };

        const handleKeyDown = (
            e: React.KeyboardEvent<HTMLInputElement | HTMLTextAreaElement>,
        ) => {
            if (onKeyDown) {
                onKeyDown(e);
            }
        };

        // 최종 에러 메시지: 내부 유효성 검사 에러 우선, 없으면 외부 errorMessage 사용
        const finalErrorMessage = internalError || errorMessage || '';

        return (
            <div className="flex h-fit w-full flex-col gap-1">
                {/* 라벨, 에러 메시지, 글자수 표시 */}
                <div className="flex items-end justify-between">
                    <div className="flex items-end gap-2 pl-1">
                        {label && (
                            <label htmlFor={id} className="text-xl font-bold">
                                {label}
                            </label>
                        )}
                        {finalErrorMessage && (
                            <span
                                className="text-fail text-md font-normal"
                                id={`${id}-error`}
                                role="alert"
                            >
                                {finalErrorMessage}
                            </span>
                        )}
                    </div>
                    {count && (
                        <span className="text-dark text-sm font-normal">
                            글자수: {charCount} / {count}
                        </span>
                    )}
                </div>

                {/* 재사용되는 Input 컴포넌트 */}
                <Input
                    ref={ref}
                    id={id}
                    placeholder={placeholder}
                    type={type}
                    value={inputValue}
                    readOnly={readOnly}
                    onChange={handleInputChange}
                    onKeyDown={handleKeyDown}
                    disabled={disabled}
                    aria-invalid={!!finalErrorMessage}
                    aria-describedby={
                        finalErrorMessage ? `${id}-error` : undefined
                    }
                    maxLength={typeof count === 'number' ? count : undefined}
                    className={`${finalErrorMessage ? 'focus:outline-fail focus:outline-2' : 'focus:outline-brand-primary-400 focus:outline-2'}`}
                />
            </div>
        );
    },
);

InputForm.displayName = 'InputForm';
export default InputForm;
