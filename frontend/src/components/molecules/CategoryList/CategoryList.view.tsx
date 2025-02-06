import { Title } from '@/components/atoms';
import CategoryItem, {
    CategoryItemProps,
} from '@/components/atoms/CategoryItem/CategoryItem';
import useEmblaCarousel from 'embla-carousel-react';
import type { EmblaOptionsType } from 'embla-carousel';

interface CategoryListViewProps {
    items: CategoryItemProps[];
}

const CategoryListView = ({ items }: CategoryListViewProps) => {
    const options: EmblaOptionsType = {
        containScroll: 'trimSnaps',
        align: 'start',
        dragFree: true,
        duration: 60,
        skipSnaps: true,
    };

    const [emblaRef] = useEmblaCarousel(options);

    return (
        <section>
            <Title label="카테고리" to="/category" />
            <section className="embla" ref={emblaRef}>
                <ul className="embla__container gap-4">
                    {items.map((item) => {
                        return (
                            <li className="embla__slide">
                                <CategoryItem {...item} />
                            </li>
                        );
                    })}
                </ul>
            </section>
        </section>
    );
};

export default CategoryListView;
