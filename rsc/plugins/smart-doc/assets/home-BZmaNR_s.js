import { U as useTranslation, j as jsxRuntimeExports, O as Typography } from './tooltip-Cuc2eMnW.js';
import { A as ActivityContent } from './activity-content-C-8DsC1V.js';
import { i as __mf_34, f as __mf_24 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { c as __mf_7, d as __mf_115 } from './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
import { $ as $api } from './styles-hjhuduQt.js';
import { A as Atom, D as DataTable, P as Plus, u as useColumnHelpers } from './hooks-BZ8JDAh7.js';
import { I as IconButton } from './useMedia-ewnkBszT.js';
import { w as useIsMobile, o as ResponsiveGrid, R as ResponsiveButton, L as ListTile } from './table-CmSAdCry.js';
import { S as SquarePen } from './square-pen-DIglC95w.js';
import { H as House } from './house-Bc8f8xL-.js';
import { u as useMutationCallbacks } from './use-mutation-callbacks-BaJ_WT_z.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-COy7YPAX.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-ByWZy8-6.js';
import './preload-helper-BgB2ycR-.js';
import './select-field-CSwbPc9r.js';
import './label-WCiYXR7h.js';
import './index-z1Zuky25.js';

function DocumentTypesListExpandedRow({
  row,
  onEdit
}) {
  const { t } = useTranslation();
  const isMobile = useIsMobile();
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(ResponsiveGrid, { children: [
    /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex justify-end col-span-full gap-2", children: isMobile && onEdit && /* @__PURE__ */ jsxRuntimeExports.jsx(
      ResponsiveButton,
      {
        variant: "tonal",
        expanded: false,
        icon: /* @__PURE__ */ jsxRuntimeExports.jsx(SquarePen, {}),
        onClick: () => onEdit?.(row.original),
        children: t("common.buttons.edit")
      }
    ) }),
    /* @__PURE__ */ jsxRuntimeExports.jsx(
      ListTile,
      {
        leading: /* @__PURE__ */ jsxRuntimeExports.jsx(Atom, {}),
        title: row.original.code,
        subtitle: row.original.name
      }
    ),
    row.original.description && /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { className: "grid gap-2 col-start-full", children: [
      /* @__PURE__ */ jsxRuntimeExports.jsx(Typography, { variant: "title-small", children: t("document-types.form.description.label") }),
      /* @__PURE__ */ jsxRuntimeExports.jsx(Typography, { children: row.original.description })
    ] })
  ] });
}

function DocumentTypesList({
  data,
  actions,
  onEdit,
  ...props
}) {
  const { t } = useTranslation();
  const isMobile = useIsMobile();
  const { columnHelper, predefinedColumns } = useColumnHelpers();
  const columns = __mf_34(() => {
    const { expand } = predefinedColumns();
    return [
      ...isMobile ? [] : [expand],
      columnHelper.accessor("code", {
        header: t("document-types.fields.code")
      }),
      columnHelper.accessor("name", {
        header: t("document-types.fields.name")
      }),
      ...isMobile ? [expand] : [
        columnHelper.display({
          id: "actions",
          cell: ({ row }) => /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex justify-end gap-1", children: onEdit && /* @__PURE__ */ jsxRuntimeExports.jsx(
            IconButton,
            {
              variant: "tonal",
              onClick: () => onEdit(row.original),
              children: /* @__PURE__ */ jsxRuntimeExports.jsx(SquarePen, {})
            }
          ) })
        })
      ]
    ];
  }, [columnHelper, predefinedColumns, isMobile, t, onEdit]);
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    DataTable,
    {
      ...props,
      options: {
        showSelected: false
      },
      showHeader: !isMobile,
      data,
      columns,
      actions: (table) => /* @__PURE__ */ jsxRuntimeExports.jsxs(jsxRuntimeExports.Fragment, { children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(__mf_7, { to: "/", children: /* @__PURE__ */ jsxRuntimeExports.jsx(IconButton, { variant: "tonal", children: /* @__PURE__ */ jsxRuntimeExports.jsx(House, {}) }) }),
        /* @__PURE__ */ jsxRuntimeExports.jsx(__mf_7, { to: "./create-document-type", children: /* @__PURE__ */ jsxRuntimeExports.jsx(IconButton, { children: /* @__PURE__ */ jsxRuntimeExports.jsx(Plus, {}) }) }),
        actions?.(table)
      ] }),
      renderExpandedRow: (row) => /* @__PURE__ */ jsxRuntimeExports.jsx(DocumentTypesListExpandedRow, { row, onEdit })
    }
  );
}

function useDocumentTypes() {
  const { data, ...query } = $api.useQuery("get", "/document-types");
  return {
    documentTypes: data ?? [],
    ...query
  };
}

const useDocumentTypesHandlers = () => {
  const { t } = useTranslation();
  const navigate = __mf_115();
  const deleteCallbacks = useMutationCallbacks("/document-types", {
    successMessage: t("document-types.messages.delete.success"),
    errorMessage: t("document-types.messages.delete.error")
  });
  const deleteMutation = $api.useMutation("delete", "/document-types/{id}", {
    ...deleteCallbacks
  });
  const handleDelete = __mf_24(
    (documentTypes) => {
      documentTypes.forEach((documentType) => {
        deleteMutation.mutate({ params: { path: { id: documentType.id } } });
      });
    },
    [deleteMutation]
  );
  const handleEdit = __mf_24(
    (documentType) => {
      navigate(`/document-types/${documentType.id}/edit`);
    },
    [navigate]
  );
  return { handleDelete, handleEdit };
};

function Home() {
  const { t } = useTranslation();
  const { documentTypes } = useDocumentTypes();
  const { handleEdit } = useDocumentTypesHandlers();
  return /* @__PURE__ */ jsxRuntimeExports.jsx(ActivityContent, { title: t("routes.document-types.title"), children: /* @__PURE__ */ jsxRuntimeExports.jsx(
    DocumentTypesList,
    {
      data: documentTypes,
      className: "max-w-3xl",
      onEdit: handleEdit
    }
  ) });
}

export { Home };
