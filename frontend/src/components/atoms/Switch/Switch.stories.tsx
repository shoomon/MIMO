import React from 'react';
import { Meta, Story } from '@storybook/react';
import Switch from './Switch';

type SwitchProps = React.ComponentProps<typeof Switch>;

export default {
    title: 'Components/Atoms/Switch',
    component: Switch,
    argTypes: {
        disabled: { control: 'boolean' },
        isactive: { control: 'boolean' },
        onClick: { action: 'clicked' },
    },
} as Meta;

const Template: Story<SwitchProps> = (args) => <Switch {...args} />;

export const Active = Template.bind({});
Active.args = {
    isactive: true,
    disabled: false,
};

export const Inactive = Template.bind({});
Inactive.args = {
    isactive: false,
    disabled: false,
};

export const Disabled = Template.bind({});
Disabled.args = {
    isactive: false,
    disabled: true,
};
