import { j as jsxRuntimeExports } from './tooltip-DiuRGInA.js';
import { C as Controller } from './index.esm-DS5i1cns.js';
import { b as SelectField } from './select-field-DdxeC6qU.js';

function SelectFormField({
  control,
  name,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    Controller,
    {
      control,
      name,
      render: ({ field, fieldState }) => /* @__PURE__ */ jsxRuntimeExports.jsx(
        SelectField,
        {
          "data-cy": field.name,
          ...props,
          "aria-invalid": fieldState.invalid,
          value: field.value ?? "",
          disabled: field.disabled,
          onValueChange: field.onChange,
          error: fieldState.invalid,
          helperText: fieldState.error?.message
        }
      )
    }
  );
}

export { SelectFormField as S };
