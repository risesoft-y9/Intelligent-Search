import { ref } from 'vue';
import { useSearchStore } from '@/store/modules/searchStore';
const searchStore = useSearchStore();
export let searchValue = ref(searchStore.searchFilterInfo.searchContent);
