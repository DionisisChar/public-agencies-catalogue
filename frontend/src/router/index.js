// Vue Router configuration will be added here later

import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import MinistryEditView from "../views/admin/ministries/MinistryEditView.vue";
import LocalAuthorityEditView from "../views/admin/localAuthorities/LocalAuthorityEditView.vue";
import OrganizationEditView from "../views/admin/organizations/OrganizationEditView.vue";
import OrganizationCreateView from "../views/admin/organizations/OrganizationCreateView.vue";
import EditOrganizationFormView from "../views/admin/organizations/EditOrganizationFormView.vue";
import OrganizationSearchView from "../views/publicUser/OrganizationSearchView.vue";
import SubmitRegistryExcelView from "../views/admin/registryImport/SubmitRegistryExcelView.vue";
import { authState } from "../services/state/authState";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/admin/ministries/edit",
    name: "ministry-edit",
    component: MinistryEditView,
  },
  {
    path: "/admin/local-authorities/edit",
    name: "LocalAuthorityEditView",
    component: LocalAuthorityEditView,
  },
  {
    path: "/admin/organizations/edit",
    name: "OrganizationEditView",
    component: OrganizationEditView,
  },
  {
    path: "/admin/organizations/create",
    name: "Organizations/create",
    component: OrganizationCreateView,
  },
  {
    path: "/admin/organizations/edit-form/:id",
    name: "EditOrganization",
    component: EditOrganizationFormView,
  },

  {
    path: "/admin/submit-excel",
    name: "SubmitRegistryExcel",
    component: SubmitRegistryExcelView,
  },

  {
    path: "/search",
    name: "OrganizationSearch",
    component: OrganizationSearchView,
  },

  {
    path: "/map",
    name: "OrganizationMapOnlyView",
    component: () => import("../views/publicUser/OrganizationMapOnlyView.vue"),
  },

  {
    path: "/organizations/:id",
    name: "OrganizationDetails",
    component: () => import("../views/publicUser/OrganizationDetailsView.vue"),
  },

  {
    path: "/login",
    name: "Login",
    component: () => import("../views/LoginView.vue"),
  },

  // άλλα routes θα μπουν αργότερα
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const user = authState.user;

  // Αν η διαδρομή ξεκινάει με /admin // .. Για την ώρα είναι καλή λύση
  if (to.path.startsWith("/admin")) {
    if (!user || user.role !== "ADMIN") {
      return next({ path: "/" }); // θα μπορουσα να κανω redirect και σε login
    }
  }
  next();
});
export default router;
