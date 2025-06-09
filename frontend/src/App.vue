<template>
  <div id="app">
    <hr />
    <router-view />
  </div>
</template>

<style scoped>
nav {
  margin-bottom: 1rem;
}
nav a {
  margin-right: 10px;
  text-decoration: none;
  color: #333;
}
nav a.router-link-active {
  font-weight: bold;
}
</style>
