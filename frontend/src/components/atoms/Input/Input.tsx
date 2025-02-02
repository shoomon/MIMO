import { forwardRef } from 'react';

interface BaseInputProps {
    id: string;
    /** 단일 행 입력일 경우 사용할 타입 */
    type?: 'text' | 'email' | 'number' | 'password';
    placeholder?: string;
    value?: string | number;
    defaultValue?: string | number;
    readOnly?: boolean;
    disabled?: boolean;
    /** multiline이 true면 textarea를 렌더링 */
    multiline?: boolean;
    /** textarea 사용 시, 행 수 지정 */
    rows?: number;
    onChange?: (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => void;
    onKeyDown?: (
        e: React.KeyboardEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => void;
    ariaLabel?: string;
    maxLength?: number;
    className: string;
}

/**
 * Input 컴포넌트는 단일 행 입력(<input>)과 다중 행 입력(<textarea>)을 모두 지원하는 재사용 가능한 입력 컴포넌트입니다.
 *
 * 이 컴포넌트는 `multiline` prop에 따라 단일 행 입력 또는 다중 행 입력을 렌더링하며,
 * 기본 스타일(텍스트, 테두리, 패딩 등)은 클래스 이름을 통해 적용됩니다.
 *
 * @component
 *
 * @example
 * // 단일 행 입력 예시:
 * <Input
 *   id="username"
 *   placeholder="사용자 이름을 입력하세요"
 *   type="text"
 *   className="my-custom-class"
 * />
 *
 * @example
 * // 다중 행 입력 예시:
 * <Input
 *   id="message"
 *   placeholder="메시지를 입력하세요"
 *   multiline={true}
 *   rows={4}
 *   className="my-custom-class"
 * />
 *
 * @param {Object} props - 컴포넌트의 props.
 * @param {string} props.id - 입력 요소의 고유 식별자.
 * @param {'text'|'email'|'number'|'password'} [props.type='text'] - 단일 행 입력일 때 사용할 HTML 입력 타입.
 * @param {string} [props.placeholder] - 입력 요소의 플레이스홀더 텍스트.
 * @param {string|number} [props.value] - 제어되는 입력 값.
 * @param {string|number} [props.defaultValue] - 비제어 입력의 기본값.
 * @param {boolean} [props.readOnly] - 입력 요소가 읽기 전용인지 여부.
 * @param {boolean} [props.disabled] - 입력 요소가 비활성화되었는지 여부.
 * @param {boolean} [props.multiline=false] - true이면 `<textarea>`를 렌더링합니다.
 * @param {number} [props.rows=6] - 다중 행 입력에서 표시할 행의 수.
 * @param {function} [props.onChange] - 입력값 변경 시 호출되는 콜백 함수.
 *   {@link React.ChangeEvent<HTMLInputElement|HTMLTextAreaElement>} 이벤트를 인자로 받습니다.
 * @param {function} [props.onKeyDown] - 키보드 이벤트 발생 시 호출되는 콜백 함수.
 *   {@link React.KeyboardEvent<HTMLInputElement|HTMLTextAreaElement>} 이벤트를 인자로 받습니다.
 * @param {string} [props.ariaLabel] - 접근성을 위한 ARIA 레이블.
 * @param {number} [props.maxLength] - 입력 가능한 최대 글자 수.
 * @param {string} props.className - 추가적인 CSS 클래스 이름.
 *
 * @returns {JSX.Element} 렌더링된 Input 컴포넌트.
 */
const Input = forwardRef<
    HTMLInputElement | HTMLTextAreaElement,
    BaseInputProps
>(
    (
        {
            id,
            type = 'text',
            placeholder,
            value,
            defaultValue,
            readOnly,
            disabled,
            multiline = false,
            rows = 6,
            onChange,
            onKeyDown,
            ariaLabel,
            maxLength,
            className,
        },
        ref,
    ) => {
        const commonClasses =
            'text-text-md text-dark w-full rounded-sm border border-gray-300 px-4 py-3 font-medium placeholder:text-gray-500 [&:not(:focus):hover]:border-gray-900' +
            ` ${className}` +
            (readOnly || disabled
                ? ' cursor-not-allowed bg-gray-100 opacity-55'
                : '');

        return (
            <div className="relative w-full">
                {multiline ? (
                    <textarea
                        ref={ref as React.Ref<HTMLTextAreaElement>}
                        id={id}
                        name={id}
                        placeholder={placeholder}
                        value={value as string | undefined}
                        defaultValue={
                            value !== undefined
                                ? undefined
                                : (defaultValue as string)
                        }
                        readOnly={readOnly}
                        disabled={disabled}
                        onChange={
                            onChange as React.ChangeEventHandler<HTMLTextAreaElement>
                        }
                        onKeyDown={
                            onKeyDown as React.KeyboardEventHandler<HTMLTextAreaElement>
                        }
                        aria-label={ariaLabel}
                        rows={rows}
                        className={`${commonClasses} resize-none`}
                    />
                ) : (
                    <input
                        maxLength={maxLength}
                        ref={ref as React.Ref<HTMLInputElement>}
                        type={type}
                        id={id}
                        name={id}
                        placeholder={placeholder}
                        value={value}
                        defaultValue={
                            value !== undefined ? undefined : defaultValue
                        }
                        readOnly={readOnly}
                        disabled={disabled}
                        onChange={
                            onChange as React.ChangeEventHandler<HTMLInputElement>
                        }
                        onKeyDown={
                            onKeyDown as React.KeyboardEventHandler<HTMLInputElement>
                        }
                        aria-label={ariaLabel}
                        className={commonClasses}
                    />
                )}
            </div>
        );
    },
);

Input.displayName = 'Input';
export default Input;
