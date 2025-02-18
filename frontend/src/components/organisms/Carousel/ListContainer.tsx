import { EmblaOptionsType } from 'embla-carousel';
import useEmblaCarousel from 'embla-carousel-react';
import { Title } from '@/components/atoms';

interface ListContainerProps {
    label: string;
    to?: string;
    gap: string;
    items: React.ReactNode[];
    content: string;
}

const options: EmblaOptionsType = {
    containScroll: 'trimSnaps',
    align: 'start',
    dragFree: true,
    duration: 60,
    skipSnaps: true,
};

function ListContainer({ gap, items, label, to, content }: ListContainerProps) {
    const [emblaRef] = useEmblaCarousel(options);

    return (
        <div className="flex w-full flex-col gap-6">
            <Title label={label} to={to} />
            <section className="embla" ref={emblaRef}>
                {items.length > 0 ? (
                    <ul className={`embla__container gap-${gap}`}>
                        {items.map((itemProps, index) => (
                            <li key={index} className="embla__slide">
                                {itemProps}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <div className="text-text flex h-36 items-center justify-center text-xl">
                        {content}
                    </div>
                )}
            </section>
        </div>
    );
}

export default ListContainer;
