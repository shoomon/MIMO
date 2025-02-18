import { forwardRef, useState } from 'react';
import Input from '../../atoms/Input/Input';

export interface InputFormProps {
    id: string;
    label?: string;
    placeholder?: string;
    defaultValue?: string | number;
    type?: 'text' | 'email' | 'number' | 'password' | 'datetime-local';
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
    multiline?: boolean;
}

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
            multiline = false,
        },
        ref,
    ) => {
        const [inputValue, setInputValue] = useState<string>(
            value !== undefined
                ? String(value)
                : defaultValue !== undefined
                  ? String(defaultValue)
                  : '',
        );

        const [charCount, setCharCount] = useState(inputValue.length);
        const [internalError, setInternalError] = useState<string>('');

        const handleInputChange = (
            e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
        ) => {
            const val = e.target.value;
            setInputValue(val);
            setCharCount(val.length);

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

        const finalErrorMessage = internalError || errorMessage || '';

        return (
            <div className="flex h-fit w-full flex-col gap-1">
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

                <Input
                    ref={ref}
                    id={id}
                    placeholder={placeholder}
                    type={type}
                    value={value}
                    readOnly={readOnly}
                    onChange={handleInputChange}
                    onKeyDown={handleKeyDown}
                    disabled={disabled}
                    multiline={multiline}
                    aria-invalid={!!finalErrorMessage}
                    aria-describedby={
                        finalErrorMessage ? `${id}-error` : undefined
                    }
                    maxLength={typeof count === 'number' ? count : undefined}
                    className={`${
                        finalErrorMessage
                            ? 'focus:outline-fail focus:outline-2'
                            : 'focus:outline-brand-primary-400 focus:outline-2'
                    }`}
                />
            </div>
        );
    },
);

InputForm.displayName = 'InputForm';
export default InputForm;
