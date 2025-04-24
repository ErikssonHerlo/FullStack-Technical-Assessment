// src/utils/getDashboardPath.ts
export const getDashboardPath = (): string => {
    const userInfo = JSON.parse(localStorage.getItem('UserInfo') || '{}');
    const userRole = userInfo?.role;
  
    switch (userRole) {
      case 'ADMIN':
        return '/admin-dashboard';
      case 'MANAGER':
        return '/manager-dashboard';
      case 'MEMBER':
        return '/member-dashboard';
      default:
        return '/'; // Fallback path if no role is found
    }
  };
  