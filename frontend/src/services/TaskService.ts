const BASE_URL = import.meta.env.VITE_BACKEND_URL;

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('authToken');
  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  };
};

// Create Task
export const createTask = async (taskData: any) => {
  const response = await fetch(`${BASE_URL}/api/v1/tasks`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(taskData),
  });
  if (!response.ok) throw new Error('Failed to create task');
  return response.json();
};

// Get All Tasks (optionally filtered)
export const fetchTasks = async (filters?: string) => {
  const url = filters ? `${BASE_URL}/api/v1/tasks?${filters}` : `${BASE_URL}/api/v1/tasks`;
  const response = await fetch(url, {
    method: 'GET',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error('Failed to fetch tasks');
  return response.json();
};

// Get Task by ID
export const fetchTaskById = async (id: number) => {
  const response = await fetch(`${BASE_URL}/api/v1/tasks/${id}`, {
    method: 'GET',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error('Failed to fetch task');
  return response.json();
};

// Update Task
export const updateTask = async (id: number, taskData: any) => {
  const response = await fetch(`${BASE_URL}/api/v1/tasks/${id}`, {
    method: 'PATCH',
    headers: getAuthHeaders(),
    body: JSON.stringify(taskData),
  });
  if (!response.ok) throw new Error('Failed to update task');
  return response.json();
};

// Delete Task
export const deleteTask = async (id: number) => {
  const response = await fetch(`${BASE_URL}/api/v1/tasks/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error('Failed to delete task');
  return response.json();
};
