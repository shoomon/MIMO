import { Title } from '@/components/atoms';
import BaseLayout from '../layouts/BaseLayout';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import ButtonLayout from '../layouts/ButtonLayout';

const TeamDetail = () => {
    return (
        <BaseLayout>
            <ButtonLayout>
                <div></div>
            </ButtonLayout>
            <BodyLayout_24>
                <div className="flex w-full flex-col gap-6">
                    <Title label="🚀나중에 시간이 된다면 타이틀 / desc로 나눠서 쓰면 예쁠듯🚀" />
                    <>
                        🚀🐾 고양이를 사랑하는 당신을 위한 최고의 모임! 🚀
                        <br />
                        🐾 우주를 유영하는 별빛처럼 반짝이는 고양이들의 매력을
                        탐험할 준비가 되셨나요? 🛸 <br />✨ 이곳은 고양이와
                        함께하는 모든 순간을 사랑하는 사람들을 위한 특별한 공간!
                        🐱🚀 <br /> 💫 냥이들의 우주, 무한한 애정! 💫 <br />
                        이곳에서는 고양이의 귀여운 모습부터 엉뚱한 행동까지,
                        모든 것을 나누고 함께 즐깁니다! � <br />
                        💖 오늘도 고양이에게 무릎을 점령당한 집사들 모여라! 😻
                        🛋️ 소파는 냥이의 왕국, 인간은 충실한 신하일 뿐! 🛋️{' '}
                        <br /> 🎾 던진 장난감은 안 물어오지만, 집사의 심장은
                        물어뜯는 냥이들! 🎾 <br /> 🚀 집사의 삶, 고양이 중심으로
                        회전 중! 🚀 <br /> 🛰️ 고양이의 하루 일과 보고서 작성! 🌕
                        한밤중, 냥줍 경험담 공유! <br />
                        🌠 별보다 빛나는 내 고양이 자랑 대잔치! <br />
                        🎨 냥이 초상화 그리기 이벤트! <br />
                        🐾 이곳에서는 오직 고양이를 위한 대화만 허용됩니다!{' '}
                        <br />
                        🐾 고양이를 처음 키우는 초보 집사부터, 평생을 함께한
                        고양이 마스터까지 모두 환영! 🏆 <br />
                        🐈 "우리는 고양이의 노예가 아닌, 충실한 수행사제다!"
                        🛐🐱 <br /> 🚀 고양이와 함께하는 이 신비로운 여정을 지금
                        시작하세요! 🚀
                    </>
                </div>
            </BodyLayout_24>
        </BaseLayout>
    );
};

export default TeamDetail;
