import { j as jsxRuntimeExports, aa as Slot, E as cn, G as cva } from './tooltip-Cuc2eMnW.js';
import { D as Dialog, i as DialogTrigger, b as DialogContent, e as DialogHeader, h as DialogTitle, c as DialogDescription, d as DialogFooter } from './dialog-DURgvrE0.js';
import { S as Separator } from './index.esm-CTXdo5zr.js';

const badgeVariants = cva(
  "inline-flex center rounded-full border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&>svg]:size-3 gap-1 [&>svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-error/20 dark:aria-invalid:ring-error/40 aria-invalid:border-error transition-[color,box-shadow] overflow-hidden",
  {
    variants: {
      variant: {
        default: "border-transparent bg-primary text-primary-foreground [a&]:hover:bg-primary/90",
        secondary: "border-transparent bg-secondary text-secondary-foreground [a&]:hover:bg-secondary/90",
        error: "border-transparent bg-error text-error-foreground [a&]:hover:bg-error/90 focus-visible:ring-error/20 dark:focus-visible:ring-error/40 dark:bg-error/60",
        outlined: "text-foreground [a&]:hover:bg-accent [a&]:hover:text-accent-foreground"
      }
    },
    defaultVariants: {
      variant: "default"
    }
  }
);
function Badge({
  className,
  variant,
  asChild = false,
  ...props
}) {
  const Comp = asChild ? Slot : "span";
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    Comp,
    {
      "data-slot": "badge",
      className: cn(badgeVariants({ variant }), className),
      ...props
    }
  );
}

function CustomDialog({
  icon,
  trigger,
  title,
  description,
  footer,
  children,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(Dialog, { ...props, children: [
    trigger && /* @__PURE__ */ jsxRuntimeExports.jsx(DialogTrigger, { children: trigger }),
    /* @__PURE__ */ jsxRuntimeExports.jsxs(DialogContent, { className: "w-full", children: [
      /* @__PURE__ */ jsxRuntimeExports.jsxs(DialogHeader, { children: [
        icon && /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "mx-auto sm:mx-0 mb-2 flex size-min", children: icon }),
        /* @__PURE__ */ jsxRuntimeExports.jsx(
          DialogTitle,
          {
            hidden: !title,
            className: "text-2xl font-bold tracking-tight",
            children: title
          }
        ),
        /* @__PURE__ */ jsxRuntimeExports.jsx(
          DialogDescription,
          {
            hidden: !description,
            className: "!mt-3 text-[15px]",
            children: description
          }
        )
      ] }),
      children && /* @__PURE__ */ jsxRuntimeExports.jsxs(jsxRuntimeExports.Fragment, { children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(Separator, {}),
        children
      ] }),
      footer && /* @__PURE__ */ jsxRuntimeExports.jsxs(jsxRuntimeExports.Fragment, { children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(Separator, {}),
        /* @__PURE__ */ jsxRuntimeExports.jsx(DialogFooter, { className: "mt-4", children: footer })
      ] })
    ] })
  ] });
}

export { Badge as B, CustomDialog as C, badgeVariants as b };
