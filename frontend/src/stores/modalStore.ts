import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
export interface GlobalModalProps {
    title?: string;
    subTitle?: string;
    onDeleteClick?: () => void;
    onConfirmClick?: () => void;
    onCancelClick?: () => void;
    confirmLabel?: string;
    cancelLabel?: string;
}

export interface ModalStateType {
    isOpen: boolean;
    modalProps: GlobalModalProps | null;
    setIsOpen: (isOpen: boolean) => void;
    openModal: (props: GlobalModalProps) => void;
    closeModal: () => void;
}

export const useModalStore = create<ModalStateType>()(
    devtools((set) => ({
        isOpen: false,
        modalProps: null,
        setIsOpen: (isOpen: boolean) => set({ isOpen }),
        openModal: (props) => set({ isOpen: true, modalProps: props }),
        closeModal: () => set({ isOpen: false, modalProps: null }),
    })),
);
