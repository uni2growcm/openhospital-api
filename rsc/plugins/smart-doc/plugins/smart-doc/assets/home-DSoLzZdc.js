import { c as createLucideIcon, U as useTranslation, j as jsxRuntimeExports, F as Button, E as cn, O as Typography, V as lodashExports } from './tooltip-DiuRGInA.js';
import { h as __mf_34, e as __mf_24, d as __mf_38, m as __mf_28 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js';
import { d as useSearchParams, e as fields, F as FILTER_SCHEMA, h as getDocumentUrl, L as Link, i as useAppSelector, $ as $api, j as useAppDispatch, k as documentsActions } from './styles-B1l4Bvay.js';
import { I as INFINITE_QUERY_DEFAULT_OPTIONS, A as ActivityContent } from './activity-content-LvLNslkQ.js';
import { s } from './standard-schema-JvgJ9ABX.js';
import { u as useForm } from './index.esm-DS5i1cns.js';
import { C as CustomDialog, B as Badge } from './dialog-custom-CB9UiEiC.js';
import { D as DateFormField, o as Calendar } from './popover-BcC5aoGW.js';
import { R as ResponsiveButton, o as ResponsiveGrid, L as ListTile, w as useIsMobile } from './table-XMtuV7nB.js';
import { D as DocumentTypeFormField, I as IMAGE_EXTENSIONS, f as formatToISODate } from './files.utils-D2dib0rH.js';
import { F as Funnel } from './funnel-CzqusM4n.js';
import { A as Atom, D as DataTable, P as Plus, u as useColumnHelpers } from './hooks-CSBi3mdy.js';
import { I as IconButton } from './useMedia--W0lxxpj.js';
import { H as House } from './house-PXy1_UYr.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-CEJIZLYi.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-CRPQeN8l.js';
import './preload-helper-Dtwkx9f4.js';
import './dialog-CB2PHVfj.js';
import './index-Dw4N6xbJ.js';
import './label-C3rC9dS4.js';
import './select-field-DdxeC6qU.js';

/**
 * @license lucide-react v0.562.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */


const __iconNode$1 = [
  [
    "path",
    {
      d: "M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0",
      key: "1nclc0"
    }
  ],
  ["circle", { cx: "12", cy: "12", r: "3", key: "1v7zrd" }]
];
const Eye = createLucideIcon("eye", __iconNode$1);

/**
 * @license lucide-react v0.562.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */


const __iconNode = [
  [
    "path",
    {
      d: "M6 22a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h8a2.4 2.4 0 0 1 1.704.706l3.588 3.588A2.4 2.4 0 0 1 20 8v12a2 2 0 0 1-2 2z",
      key: "1oefj6"
    }
  ],
  ["path", { d: "M14 2v5a1 1 0 0 0 1 1h5", key: "wfsgrz" }]
];
const File = createLucideIcon("file", __iconNode);

function useSearchParam(paramName) {
  const [searchParams] = useSearchParams();
  const searchParam = __mf_34(
    () => searchParams.get(paramName),
    [searchParams, paramName]
  );
  return searchParam;
}

function DocumentsFilterDialog({
  filter,
  onFilterChange,
  ...props
}) {
  const { t } = useTranslation();
  const { control, handleSubmit, watch, reset } = useForm({
    resolver: s(FILTER_SCHEMA),
    defaultValues: filter
  });
  const values = watch();
  const filterCount = __mf_34(
    () => Object.values(values).filter(Boolean).length,
    [values]
  );
  const handleFilterSubmit = __mf_24(
    (values2) => {
      onFilterChange(values2);
      props.onOpenChange?.(false);
    },
    [onFilterChange, props.onOpenChange]
  );
  const handleReset = __mf_24(() => {
    const value = { fromDate: null, toDate: null, type: null };
    onFilterChange(value);
    reset(value);
    props.onOpenChange?.(false);
  }, [onFilterChange, props.onOpenChange, reset]);
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    CustomDialog,
    {
      title: t("documents.filter.title"),
      trigger: /* @__PURE__ */ jsxRuntimeExports.jsxs(
        ResponsiveButton,
        {
          icon: /* @__PURE__ */ jsxRuntimeExports.jsx(Funnel, {}),
          variant: "tonal",
          "data-cy": "document-filter-button",
          children: [
            t("common.buttons.filter"),
            filterCount > 0 && /* @__PURE__ */ jsxRuntimeExports.jsx(Badge, { children: filterCount })
          ]
        }
      ),
      ...props,
      children: /* @__PURE__ */ jsxRuntimeExports.jsxs(
        "form",
        {
          className: cn("form-grid-layout w-full"),
          "data-cy": "filter-documents-form",
          onSubmit: handleSubmit(handleFilterSubmit),
          children: [
            /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { "data-slot": "form-fields", children: [
              /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex justify-end w-full col-span-full", children: /* @__PURE__ */ jsxRuntimeExports.jsx(
                Button,
                {
                  variant: "tonal",
                  onClick: handleReset,
                  "data-cy": "document-reset-filter-button",
                  children: t("documents.filter.reset")
                }
              ) }),
              /* @__PURE__ */ jsxRuntimeExports.jsx(
                DocumentTypeFormField,
                {
                  mode: "single",
                  label: t(fields.type.label),
                  placeholder: t(fields.type.placeholder),
                  control,
                  name: "type"
                }
              ),
              /* @__PURE__ */ jsxRuntimeExports.jsx(
                DateFormField,
                {
                  label: t(fields.fromDate.label),
                  placeholder: t(fields.fromDate.placeholder),
                  control,
                  name: "fromDate"
                }
              ),
              /* @__PURE__ */ jsxRuntimeExports.jsx(
                DateFormField,
                {
                  label: t(fields.toDate.label),
                  placeholder: t(fields.toDate.placeholder),
                  control,
                  name: "toDate"
                }
              )
            ] }),
            /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { "data-slot": "form-actions", className: "px-0", children: [
              /* @__PURE__ */ jsxRuntimeExports.jsx(
                Button,
                {
                  className: "px-8",
                  variant: "tonal",
                  onClick: () => props.onOpenChange?.(false),
                  children: t("common.buttons.cancel")
                }
              ),
              /* @__PURE__ */ jsxRuntimeExports.jsx(Button, { type: "submit", className: "px-8", "data-cy": "submit-filter-button", children: t("documents.filter.apply") })
            ] })
          ]
        }
      )
    }
  );
}

function DocumentPreviewDialog({
  title,
  document,
  ...props
}) {
  const { t } = useTranslation();
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    CustomDialog,
    {
      ...props,
      title: title || t("documents.labels.preview"),
      trigger: props.trigger || /* @__PURE__ */ jsxRuntimeExports.jsx(IconButton, { variant: "tonal", children: /* @__PURE__ */ jsxRuntimeExports.jsx(Eye, {}) }),
      children: /* @__PURE__ */ jsxRuntimeExports.jsx(
        "img",
        {
          src: getDocumentUrl(document),
          alt: "Document preview",
          className: "size-full max-w-[512px] max-h-[512px] object-contain"
        }
      )
    }
  );
}

function DocumentsListExpandedRow({
  row,
  onView
}) {
  const { t } = useTranslation();
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(ResponsiveGrid, { children: [
    /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex justify-end col-span-full gap-2", children: onView && /* @__PURE__ */ jsxRuntimeExports.jsx(
      DocumentPreviewDialog,
      {
        trigger: /* @__PURE__ */ jsxRuntimeExports.jsxs(
          Button,
          {
            variant: "tonal",
            color: "secondary",
            onClick: () => onView?.(row.original),
            children: [
              /* @__PURE__ */ jsxRuntimeExports.jsx(Eye, {}),
              t("common.buttons.view-details")
            ]
          }
        ),
        document: row.original
      }
    ) }),
    /* @__PURE__ */ jsxRuntimeExports.jsx(
      ListTile,
      {
        leading: /* @__PURE__ */ jsxRuntimeExports.jsx(Atom, {}),
        title: t("documents.fields.type"),
        subtitle: row.original.type
      }
    ),
    /* @__PURE__ */ jsxRuntimeExports.jsx(
      ListTile,
      {
        leading: /* @__PURE__ */ jsxRuntimeExports.jsx(Calendar, {}),
        title: t("documents.fields.date"),
        subtitle: row.original.date
      }
    )
  ] });
}

function DocumentsList({
  data,
  actions,
  onLoadMore,
  isLoadingMore,
  onView,
  ...props
}) {
  const { t } = useTranslation();
  const isMobile = useIsMobile();
  const { columnHelper, predefinedColumns } = useColumnHelpers();
  const columns = __mf_34(() => {
    const { expand } = predefinedColumns();
    return [
      columnHelper.accessor("id", {
        id: "name",
        header: t("documents.fields.name"),
        cell: (props2) => {
          const document = props2.row.original;
          return /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { className: "flex gap-2 items-center", children: [
            IMAGE_EXTENSIONS.some(
              (ext) => document.id?.toLowerCase()?.endsWith(ext)
            ) ? /* @__PURE__ */ jsxRuntimeExports.jsx(
              "img",
              {
                src: getDocumentUrl(document),
                alt: "Document",
                className: "w-10 lg:w-12 aspect-square rounded object-contain"
              }
            ) : /* @__PURE__ */ jsxRuntimeExports.jsx(File, { className: "text-muted-foreground mx-2 lg:mx-3" }),
            /* @__PURE__ */ jsxRuntimeExports.jsx(Typography, { children: document.id })
          ] });
        }
      }),
      ...isMobile ? [expand] : [
        columnHelper.accessor("type", {
          header: t("documents.fields.type")
        }),
        columnHelper.accessor("date", {
          header: t("documents.fields.date")
        }),
        columnHelper.display({
          id: "actions",
          cell: ({ row }) => /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex justify-end gap-1", children: /* @__PURE__ */ jsxRuntimeExports.jsx(
            DocumentPreviewDialog,
            {
              trigger: /* @__PURE__ */ jsxRuntimeExports.jsx(
                IconButton,
                {
                  variant: "tonal",
                  color: "secondary",
                  onClick: () => onView?.(row.original),
                  "data-cy": "preview-document-button",
                  children: /* @__PURE__ */ jsxRuntimeExports.jsx(Eye, {})
                }
              ),
              document: row.original
            }
          ) })
        })
      ]
    ];
  }, [columnHelper, predefinedColumns, isMobile, onView, t]);
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    DataTable,
    {
      ...props,
      options: {
        showSelected: false
      },
      data,
      columns,
      onLoadMore,
      isLoadingMore,
      showHeader: !isMobile,
      actions: (table) => /* @__PURE__ */ jsxRuntimeExports.jsxs(jsxRuntimeExports.Fragment, { children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(Link, { to: "/", children: /* @__PURE__ */ jsxRuntimeExports.jsx(IconButton, { variant: "tonal", children: /* @__PURE__ */ jsxRuntimeExports.jsx(House, {}) }) }),
        /* @__PURE__ */ jsxRuntimeExports.jsx(Link, { to: "./upload-document", children: /* @__PURE__ */ jsxRuntimeExports.jsx(IconButton, { children: /* @__PURE__ */ jsxRuntimeExports.jsx(Plus, {}) }) }),
        actions?.(table)
      ] }),
      renderExpandedRow: isMobile ? (row) => /* @__PURE__ */ jsxRuntimeExports.jsx(DocumentsListExpandedRow, { row, onView }) : void 0
    }
  );
}

function useInfiniteDocuments() {
  const filter = useAppSelector((state) => state.documents.filter);
  const { data, ...query } = $api.useInfiniteQuery(
    "get",
    "/documents",
    {
      params: {
        query: {
          size: 10,
          ...filter,
          fromDate: formatToISODate(filter.fromDate) || void 0,
          toDate: formatToISODate(filter.toDate) || void 0,
          personId: filter.personId ?? 0
        }
      }
    },
    INFINITE_QUERY_DEFAULT_OPTIONS
  );
  const documents = __mf_34(
    () => data?.pages?.flatMap((page) => page?.data ?? []) ?? [],
    [data]
  );
  return {
    documents,
    ...query
  };
}

function Home() {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const filter = useAppSelector((state) => state.documents.filter);
  const [open, setOpen] = __mf_38(false);
  const personId = useSearchParam("client");
  const { data: person } = $api.useQuery(
    "get",
    "/persons/{id}",
    { params: { path: { id: personId ?? "0" } } },
    { enabled: !!personId }
  );
  const { documents, fetchNextPage, hasNextPage } = useInfiniteDocuments();
  __mf_28(() => {
    if (person) {
      dispatch(
        documentsActions.updateFilter({ personId: parseInt(person.pid, 10) })
      );
    }
  }, [dispatch, person]);
  const handleLoadMore = __mf_24(() => {
    if (hasNextPage) {
      fetchNextPage();
    }
  }, [fetchNextPage, hasNextPage]);
  const handleFilterChange = __mf_24(
    (filter2) => {
      dispatch(
        documentsActions.updateFilter({
          type: filter2.type ?? void 0,
          fromDate: filter2.fromDate ?? void 0,
          toDate: filter2.toDate ?? void 0
        })
      );
    },
    [dispatch]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    ActivityContent,
    {
      title: t("routes.documents.title", { client: person?.name ?? "-" }),
      "data-cy": "document-list-activity",
      children: /* @__PURE__ */ jsxRuntimeExports.jsx(
        DocumentsList,
        {
          data: documents,
          onLoadMore: hasNextPage ? handleLoadMore : void 0,
          className: "max-w-3xl",
          onView: () => {
          },
          actions: () => /* @__PURE__ */ jsxRuntimeExports.jsx(
            DocumentsFilterDialog,
            {
              open,
              onOpenChange: setOpen,
              filter: lodashExports.omit(filter, ["personId"]),
              onFilterChange: handleFilterChange
            }
          )
        }
      )
    }
  );
}

export { Home };
