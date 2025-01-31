import type { Preview } from "@storybook/react";
import '../src/index.css'; // Tailwind 테마 로드

const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
  },
};

export default preview;
