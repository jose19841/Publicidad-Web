import getCategoriesService from "../services/getCategoriesService";
import useTable from "../../../hooks/useTable";

export default function useCategoriesTable() {
  return useTable(getCategoriesService, 10);
}
