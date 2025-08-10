import { create } from "zustand"

interface Task {
  id: string
  title: string
  description: string
  status: "TODO" | "IN_PROGRESS" | "IN_REVIEW" | "COMPLETED"
  priority: "LOW" | "MEDIUM" | "HIGH"
  assigneeId: string
  projectId: string
  dueDate: string
  createdAt: string
  updatedAt: string
}

interface TaskState {
  tasks: Task[]
  isLoading: boolean
  fetchTasks: () => Promise<void>
  createTask: (task: Omit<Task, "id" | "createdAt" | "updatedAt">) => Promise<void>
  updateTask: (id: string, updates: Partial<Task>) => Promise<void>
  updateTaskStatus: (id: string, status: Task["status"]) => Promise<void>
  deleteTask: (id: string) => Promise<void>
}

// Mock data
const mockTasks: Task[] = [
  {
    id: "1",
    title: "Design Homepage Layout",
    description: "Create wireframes and mockups for the new homepage design",
    status: "IN_PROGRESS",
    priority: "HIGH",
    assigneeId: "1",
    projectId: "1",
    dueDate: "2024-02-01T00:00:00Z",
    createdAt: "2024-01-15T10:00:00Z",
    updatedAt: "2024-01-20T14:30:00Z",
  },
  {
    id: "2",
    title: "Implement User Authentication",
    description: "Set up JWT-based authentication system with login and registration",
    status: "TODO",
    priority: "HIGH",
    assigneeId: "2",
    projectId: "2",
    dueDate: "2024-02-05T00:00:00Z",
    createdAt: "2024-01-16T09:00:00Z",
    updatedAt: "2024-01-16T09:00:00Z",
  },
  {
    id: "3",
    title: "Database Schema Design",
    description: "Design and implement the database schema for user management",
    status: "COMPLETED",
    priority: "MEDIUM",
    assigneeId: "1",
    projectId: "3",
    dueDate: "2024-01-25T00:00:00Z",
    createdAt: "2024-01-10T08:00:00Z",
    updatedAt: "2024-01-24T16:00:00Z",
  },
  {
    id: "4",
    title: "Content Strategy Planning",
    description: "Develop content calendar and strategy for Q1 marketing campaign",
    status: "IN_REVIEW",
    priority: "MEDIUM",
    assigneeId: "3",
    projectId: "4",
    dueDate: "2024-01-30T00:00:00Z",
    createdAt: "2024-01-12T11:00:00Z",
    updatedAt: "2024-01-22T13:20:00Z",
  },
  {
    id: "5",
    title: "API Documentation",
    description: "Write comprehensive API documentation for the mobile app endpoints",
    status: "TODO",
    priority: "LOW",
    assigneeId: "1",
    projectId: "2",
    dueDate: "2024-02-10T00:00:00Z",
    createdAt: "2024-01-18T14:00:00Z",
    updatedAt: "2024-01-18T14:00:00Z",
  },
]

// Update the API base URL
const API_BASE_URL = "http://localhost:9999"

export const useTaskStore = create<TaskState>((set, get) => ({
  tasks: [],
  isLoading: false,

  fetchTasks: async () => {
    set({ isLoading: true })
    try {
      const response = await fetch(`${API_BASE_URL}/api/v1/task`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })

      if (!response.ok) {
        throw new Error("Failed to fetch tasks")
      }

      // For now, use mock data
      await new Promise((resolve) => setTimeout(resolve, 500))
      set({ tasks: mockTasks, isLoading: false })
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  createTask: async (taskData) => {
    set({ isLoading: true })
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 500))
      const newTask: Task = {
        ...taskData,
        id: Date.now().toString(),
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      }
      set((state) => ({
        tasks: [...state.tasks, newTask],
        isLoading: false,
      }))
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  updateTask: async (id: string, updates) => {
    set({ isLoading: true })
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 300))
      set((state) => ({
        tasks: state.tasks.map((t) => (t.id === id ? { ...t, ...updates, updatedAt: new Date().toISOString() } : t)),
        isLoading: false,
      }))
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  updateTaskStatus: async (id: string, status) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/v1/task/${id}/status`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify({ status }),
      })

      if (!response.ok) {
        throw new Error("Failed to update task status")
      }

      // Update local state
      await new Promise((resolve) => setTimeout(resolve, 200))
      set((state) => ({
        tasks: state.tasks.map((t) => (t.id === id ? { ...t, status, updatedAt: new Date().toISOString() } : t)),
      }))
    } catch (error) {
      throw error
    }
  },

  deleteTask: async (id: string) => {
    set({ isLoading: true })
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 300))
      set((state) => ({
        tasks: state.tasks.filter((t) => t.id !== id),
        isLoading: false,
      }))
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },
}))
