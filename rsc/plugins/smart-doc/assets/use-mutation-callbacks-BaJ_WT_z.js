import { n as useQueryClient } from './styles-hjhuduQt.js';
import { f as __mf_24 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { q as queryMatchPredicate } from './activity-content-C-8DsC1V.js';
import { U as useTranslation, Q as toasts } from './tooltip-Cuc2eMnW.js';
import './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';

function useMutationCallbacks(path, {
  successMessage,
  errorMessage,
  onSuccess,
  onError,
  onMutate,
  extraKeys = []
}) {
  const { t } = useTranslation();
  const client = useQueryClient();
  const handleSuccess = __mf_24(
    async (_data) => {
      const message = successMessage || t("common.operations.success.message");
      toasts.success({ description: message, options: { id: path } });
      await client.invalidateQueries({
        predicate: queryMatchPredicate(
          ...Array.from(/* @__PURE__ */ new Set([path, ...extraKeys]))
        )
      });
      onSuccess?.(_data);
    },
    [successMessage, t, client, path, extraKeys, onSuccess]
  );
  const handleError = __mf_24(
    (error) => {
      onError?.(error);
      const message = errorMessage || t("common.operations.error.message");
      toasts.error({
        description: error?.title || message,
        options: { id: path }
      });
    },
    [errorMessage, t, path, onError]
  );
  const handleLoading = __mf_24(() => {
    onMutate?.();
    toasts.loading({
      description: t("common.operations.loading.message"),
      options: { id: path, duration: Infinity }
    });
  }, [t, path, onMutate]);
  return {
    onSuccess: handleSuccess,
    onError: handleError,
    onMutate: handleLoading
  };
}

export { useMutationCallbacks as u };
