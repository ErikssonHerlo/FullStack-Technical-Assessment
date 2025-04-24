const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const handleResponse = async (response: Response) => {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    const message = errorData.message || 'An unexpected error occurred';
    throw new Error(message);
  }
  return response.json();
};

export const loginRequest = async (email: string, password: string) => {
  const response = await fetch(`${API_BASE_URL}/v1/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  });
  return handleResponse(response);
};

export const fetchUserInfo = async (token: string) => {
  const response = await fetch(`${API_BASE_URL}/v1/users/me`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  return handleResponse(response);
};

export const registerRequest = async (userData: {
    email: string;
    firstName: string;
    lastName: string;
    password: string;
}) => {
  const response = await fetch(`${API_BASE_URL}/v1/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });
  return handleResponse(response);
};
