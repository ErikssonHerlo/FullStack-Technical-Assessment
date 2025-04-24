const BASE_URL = import.meta.env.VITE_BACKEND_URL;

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('authToken');
  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  };
};

// Create User
export const createUser = async (userData: any) => {
  const response = await fetch(`${BASE_URL}/api/v1/users`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(userData),
  });
  if (!response.ok) throw new Error('Failed to create user');
  return response.json();
};

// Get All Users
export const fetchUsers = async () => {
  const response = await fetch(`${BASE_URL}/api/v1/users`, {
    method: 'GET',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error('Failed to fetch users');
  return response.json();
};

// Get User by ID
export const fetchUserById = async (id: number) => {
  const response = await fetch(`${BASE_URL}/api/v1/users/${id}`, {
    method: 'GET',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error('Failed to fetch user');
  return response.json();
};

// Update User
export const updateUser = async (id: number, userData: any) => {
  const response = await fetch(`${BASE_URL}/api/v1/users/${id}`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify(userData),
  });
  if (!response.ok) throw new Error('Failed to update user');
  return response.json();
};

// Delete User
export const deleteUser = async (id: number) => {
  const response = await fetch(`${BASE_URL}/api/v1/users/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error('Failed to delete user');
  return response.json();
};
