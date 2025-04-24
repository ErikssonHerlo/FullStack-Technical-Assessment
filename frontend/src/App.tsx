import { useEffect, useState } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';

import Loader from './common/Loader';
import PageTitle from './components/PageTitle';
import SignIn from './pages/Authentication/SignIn';
import SignUp from './pages/Authentication/SignUp';

import AdminDashboard from './pages/Dashboard/AdminDashboard';
import ManagerDashboard from './pages/Dashboard/ManagerDashboard';
import MemberDashboard from './pages/Dashboard/MemberDashboard';

// import TaskList from './pages/Tasks/TaskList';
// import TaskCreation from './pages/Tasks/TaskCreation';

// import UserList from './pages/Users/UserList';
// import UserCreation from './pages/Users/UserCreation';

import Profile from './pages/Profile';
import Settings from './pages/Settings';
import NotFound from './pages/NotFound';

function App() {
  const [loading, setLoading] = useState<boolean>(true);
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  useEffect(() => {
    setTimeout(() => setLoading(false), 1000);
  }, []);

  return loading ? (
    <Loader />
  ) : (
    <>
      <Routes>
        {/* Public Routes */}
        <Route
          index
          element={
            <>
              <PageTitle title="Sign In | Task Management App" />
              <SignIn />
            </>
          }
        />
        <Route
          path="/auth/signin"
          element={
            <>
              <PageTitle title="Sign In | Task Management App" />
              <SignIn />
            </>
          }
        />
        <Route
          path="/auth/signup"
          element={
            <>
              <PageTitle title="Sign Up | Task Management App" />
              <SignUp />
            </>
          }
        />

        {/* Dashboards by Role */}
        <Route
          path="/admin-dashboard"
          element={
            <>
              <PageTitle title="Admin Dashboard | Task Management App" />
              <AdminDashboard />
            </>
          }
        />
        <Route
          path="/manager-dashboard"
          element={
            <>
              <PageTitle title="Manager Dashboard | Task Management App" />
              <ManagerDashboard />
            </>
          }
        />
        <Route
          path="/member-dashboard"
          element={
            <>
              <PageTitle title="Member Dashboard | Task Management App" />
              <MemberDashboard />
            </>
          }
        />

        

        {/* Profile & Settings */}
        <Route
          path="/profile"
          element={
            <>
              <PageTitle title="My Profile | Task Management App" />
              <Profile />
            </>
          }
        />
        <Route
          path="/settings"
          element={
            <>
              <PageTitle title="Settings | Task Management App" />
              <Settings />
            </>
          }
        />

        {/* Fallback - 404 */}
        <Route
          path="*"
          element={
            <>
              <PageTitle title="Page Not Found | Task Management App" />
              <NotFound />
            </>
          }
        />
      </Routes>
    </>
  );
}

export default App;
