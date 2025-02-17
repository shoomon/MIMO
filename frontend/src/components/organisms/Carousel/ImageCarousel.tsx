import { EmblaOptionsType } from 'embla-carousel';
import useEmblaCarousel from 'embla-carousel-react';

interface ImageCarouselProps {
    images: string[];
    gap?: string;
}

const options: EmblaOptionsType = {
    containScroll: 'trimSnaps',
    align: 'start',
    dragFree: true,
    duration: 60,
    skipSnaps: true,
};

function ImageCarousel({ images, gap = '4' }: ImageCarouselProps) {
    const [emblaRef] = useEmblaCarousel(options);

    return (
        <section className="embla" ref={emblaRef}>
            {images.length > 0 ? (
                <ul className={`embla__container gap-${gap}`}>
                    {images.map((src, index) => (
                        <li key={index} className="embla__slide">
                            <img
                                src={src}
                                alt={`Slide ${index + 1}`}
                                className="h-[150px] w-[150px] object-cover"
                            />
                        </li>
                    ))}
                </ul>
            ) : (
                <div className="text-text flex items-center justify-center text-xl"></div>
            )}
        </section>
    );
}

export default ImageCarousel;
