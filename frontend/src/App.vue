<!-- src/App.vue -->
<template>
  <div id="app">
    <!-- 🔷 Top Header Bar -->
    <header class="top-header">
      <div class="logo-title">Κατάλογος Δημοσίων Οργανισμών και Υπηρεσιών</div>
      <div class="header-center">ΕΑΠ - Πτυχιακή</div>
      <div class="login-section">
        <template v-if="user">
          <div class="user-box" @click="toggleDropdown">
            👤 {{ user.fullName.split(" ")[0] }} ▼
          </div>
          <div class="dropdown" v-if="dropdownVisible">
            <p><strong>Email:</strong> {{ user.email }}</p>
            <p>
              <strong>{{ user.role }}</strong>
            </p>
            <button @click="logout">Αποσύνδεση</button>
          </div>
        </template>
        <template v-else>
          <button @click="goToLogin">Είσοδος</button>
        </template>
      </div>
    </header>

    <!-- 🔹 Info Bar κάτω από το Header -->
    <nav class="info-bar">
      <span>Info</span>
      <span>Πτυχιακή</span>
      <span>API Docs</span>
    </nav>

    <div class="layout-container">
      <!-- 🔻 Sidebar (Navigation) -->
      <aside class="sidebar" :class="{ collapsed: isCollapsed }">
        <!-- ⬅️ Collapse Button -->
        <div class="collapse-button" @click="toggleSidebar">
          <span v-if="isCollapsed">▶</span>
          <span v-else>◀</span>
        </div>

        <!-- 🔸 Menu Items -->
        <ul>
          <!-- Home Page -->
          <li
            class="menu-item"
            data-tooltip="Αρχική"
            @click="$router.push('/')"
          >
            <i class="icon">🏠</i>
            <span v-if="!isCollapsed">Αρχική</span>
          </li>

          <!-- Aναζήτηση (All Users) -->
          <li
            class="menu-item"
            data-tooltip="Αναζήτηση"
            @click="$router.push('/search')"
          >
            <i class="icon">🔍</i>
            <span v-if="!isCollapsed">Αναζήτηση</span>
          </li>

          <!-- Πλοήγηση μέσω Χάρτη -->
          <li
            class="menu-item"
            data-tooltip="Πλοήγηση μέσω Χάρτη"
            @click="$router.push('/map')"
          >
            <i class="icon">🗺️</i>
            <span v-if="!isCollapsed">Πλοήγηση μέσω Χάρτη</span>
          </li>

          <!-- Διαχείριση Οργανισμών (Admin Only) -->
          <li
            class="menu-item with-submenu"
            data-tooltip="Οργανισμοί"
            v-if="isAdmin"
          >
            <i class="icon">🏢</i>
            <span v-if="!isCollapsed">Οργανισμοί</span>
            <ul class="submenu" v-if="!isCollapsed">
              <li @click="$router.push('/admin/organizations/edit')">
                🛠️ Διαχείριση Οργανισμών
              </li>
            </ul>
          </li>

          <!-- Διαχείριση Υπουργείων (Admin Only) -->
          <li
            class="menu-item with-submenu"
            data-tooltip="Υπουργεία"
            v-if="isAdmin"
          >
            <i class="icon">🏛️</i>
            <span v-if="!isCollapsed">Υπουργεία</span>
            <ul class="submenu" v-if="!isCollapsed">
              <li @click="$router.push('/admin/ministries/edit')">
                🛠️ Διαχείριση Υπουργείων
              </li>
            </ul>
          </li>

          <!-- Διαχείριση ΟΤΑ (Admin Only) -->
          <li class="menu-item with-submenu" data-tooltip="ΟΤΑ" v-if="isAdmin">
            <i class="icon">🌍</i>
            <span v-if="!isCollapsed">ΟΤΑ</span>
            <ul class="submenu" v-if="!isCollapsed">
              <li @click="$router.push(`/admin/local-authorities/edit`)">
                🛠️ Διαχείριση ΟΤΑ
              </li>
            </ul>
          </li>

          <!-- Εισαγωγή Excel Αρχείου (Admin Only) -->
          <li
            class="menu-item"
            data-tooltip="Εισαγωγή Excel"
            @click="$router.push('/admin/submit-excel')"
            v-if="isAdmin"
          >
            <i class="icon">📥</i>
            <span v-if="!isCollapsed">Διαχείριση Βάσης</span>
          </li>
        </ul>
      </aside>

      <!-- 🔸 Main window που αλλάζει ανάλογα με το route -->
      <main class="main-view">
        <router-view />
      </main>
    </div>

    <!-- 🔽 Footer -->
    <footer class="footer">
      <p>Προς Υλοποίηση...</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { authState } from "./services/state/authState";
import { logoutUser } from "./services/auth/AuthService";

const router = useRouter();
const isCollapsed = ref(true);

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value;
};

const goToLogin = () => {
  router.push("/login");
};

const user = computed(() => authState.user);
const isAdmin = computed(() => user.value?.role === "ADMIN");
const dropdownVisible = ref(false);

const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
};

const logout = async () => {
  await logoutUser();
  authState.user = null;
  router.push("/login");
};
</script>

<style scoped>
#app {
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

/* 🔷 Header */
.top-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #001b3b;
  color: white;
  padding: 10px 20px;
}

/* 🔹 Info Bar */
.info-bar {
  display: flex;
  justify-content: center;
  gap: 20px;
  background-color: #e0e0e0;
  padding: 8px;
}

/* Layout */
.layout-container {
  display: flex;
  flex: 1;
}

/* login */
.login-section button {
  background-color: #ffffff;
  color: #001b3b;
  border: 1px solid #ffffff;
  padding: 6px 16px;
  font-size: 1rem;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.25s ease;
  font-weight: bold;
}

.login-section button:hover {
  background-color: #207ad4;
  color: white;
  border-color: #207ad4;
}

/* 🔻 Sidebar */
.sidebar {
  width: 240px;
  background-color: #001b3b;
  padding: 8px;
  transition: width 0.3s;
  position: relative;
  z-index: 1000;
}

.sidebar.collapsed {
  width: 60px;
}

/* 📌 Collapse button */
.collapse-button {
  position: absolute;
  right: -12px;
  top: 50%;
  transform: translateY(-50%);
  background-color: #cccccc;
  padding: 5px 8px;
  border-radius: 0 5px 5px 0;
  cursor: pointer;
}

/* 🔸 Menu Item */
.menu-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  border-bottom: 1px solid white;
  padding: 10px;
  border-radius: 6px;
  color: white;
  cursor: pointer;
}

.menu-item:hover {
  background-color: #207ad4;
}

.icon {
  width: 30px;
  text-align: center;
  color: white;
}

/* 🟦 Tooltip για Collapsed Sidebar */
.sidebar.collapsed .menu-item {
  position: relative;
}

.sidebar.collapsed .menu-item:hover::after {
  content: attr(data-tooltip);
  position: absolute;
  left: 100%;
  top: 50%;
  transform: translateY(-50%);
  margin-left: 8px;
  background-color: #207ad4;
  color: white;
  padding: 6px 10px;
  border-radius: 4px;
  white-space: nowrap;
  z-index: 1000;
  font-size: 14px;
}

/* 🔽 Submenu (βγαίνει δεξιά) */
.with-submenu {
  position: relative;
}

.with-submenu:hover .submenu {
  display: block;
}

.submenu {
  display: none;
  position: absolute;
  left: 100%;
  top: 0;
  background: #001b3b;
  border: 1px solid #ccc;
  padding: 8px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.2);
  white-space: nowrap;
}

.submenu li {
  padding: 5px 10px;
  cursor: pointer;
}

.submenu li:hover {
  background-color: #207ad4;
}

/* 🔸 Main View */
.main-view {
  flex: 1;
  padding: 20px;
  background-color: #fafafa;
}

/* 🔽 Footer */
.footer {
  background-color: #dddddd;
  padding: 8px;
  text-align: center;
}

.user-box {
  cursor: pointer;
  font-weight: bold;
  background-color: #ffffff;
  color: #001b3b;
  padding: 6px 16px;
  border-radius: 6px;
}

.dropdown {
  position: absolute;
  right: 20px;
  top: 60px;
  background: white;
  border: 1px solid #ccc;
  padding: 12px;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  z-index: 100;
  min-width: 220px;
  color: #001b3b;
}

.dropdown button {
  margin-top: 1rem;
  width: 100%;
  padding: 0.5rem;
  background-color: #cc0000;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
</style>
