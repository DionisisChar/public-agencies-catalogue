import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'

// apo primevue
import PrimeVue from 'primevue/config';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Paginator from 'primevue/paginator';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

import 'primevue/resources/themes/saga-blue/theme.css'; // ή άλλο theme
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';

// Leaflet
import 'leaflet/dist/leaflet.css';


const app = createApp(App)
app.use(router)
// για primeVue χρήση
app.use(PrimeVue);

app.component('DataTable', DataTable);
app.component('Column', Column);
app.component('InputText', InputText);
app.component('Button', Button);
app.mount('#app')