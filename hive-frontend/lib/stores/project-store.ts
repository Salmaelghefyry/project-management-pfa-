import { create } from "zustand"

interface Project {
  id: string
  name: string
  description: string
  status: "ACTIVE" | "PLANNING" | "ON_HOLD" | "COMPLETED"
  createdAt: string
  updatedAt: string
  memberCount: number
  progress: number
  tags: string[]
  leaderId: string
}

interface ProjectState {
  projects: Project[]
  currentProject: Project | null
  isLoading: boolean
  fetchProjects: () => Promise<void>
  fetchProject: (id: string) => Promise<void>
  createProject: (project: Omit<Project, "id" | "createdAt" | "updatedAt">) => Promise<void>
  updateProject: (id: string, updates: Partial<Project>) => Promise<void>
  deleteProject: (id: string) => Promise<void>
}

const API_BASE_URL = "http://localhost:9999"

// Mock data
const mockProjects: Project[] = [
  {
    id: "1",
    name: "Website Redesign",
    description: "Complete overhaul of the company website with modern design and improved UX",
    status: "ACTIVE",
    createdAt: "2024-01-15T10:00:00Z",
    updatedAt: "2024-01-20T14:30:00Z",
    memberCount: 5,
    progress: 65,
    tags: ["Design", "Frontend", "UX"],
    leaderId: "1",
  },
  {
    id: "2",
    name: "Mobile App Development",
    description: "Native mobile application for iOS and Android platforms",
    status: "PLANNING",
    createdAt: "2024-01-10T09:00:00Z",
    updatedAt: "2024-01-18T16:45:00Z",
    memberCount: 8,
    progress: 25,
    tags: ["Mobile", "React Native", "API"],
    leaderId: "2",
  },
  {
    id: "3",
    name: "Database Migration",
    description: "Migrate legacy database to modern cloud infrastructure",
    status: "COMPLETED",
    createdAt: "2023-12-01T08:00:00Z",
    updatedAt: "2024-01-05T12:00:00Z",
    memberCount: 3,
    progress: 100,
    tags: ["Backend", "Database", "Cloud"],
    leaderId: "3",
  },
  {
    id: "4",
    name: "Marketing Campaign",
    description: "Q1 digital marketing campaign for product launch",
    status: "ACTIVE",
    createdAt: "2024-01-08T11:00:00Z",
    updatedAt: "2024-01-19T13:20:00Z",
    memberCount: 4,
    progress: 40,
    tags: ["Marketing", "Content", "Social Media"],
    leaderId: "4",
  },
]

export const useProjectStore = create<ProjectState>((set, get) => ({
  projects: [],
  currentProject: null,
  isLoading: false,

  fetchProjects: async () => {
    set({ isLoading: true })
    try {
      const response = await fetch(`${API_BASE_URL}/api/v1/project`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })

      if (!response.ok) {
        throw new Error("Failed to fetch projects")
      }

      // For now, use mock data
      await new Promise((resolve) => setTimeout(resolve, 500))
      set({ projects: mockProjects, isLoading: false })
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  fetchProject: async (id: string) => {
    set({ isLoading: true })
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 300))
      const project = mockProjects.find((p) => p.id === id)
      set({ currentProject: project || null, isLoading: false })
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  createProject: async (projectData) => {
    set({ isLoading: true })
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 500))
      const newProject: Project = {
        ...projectData,
        id: Date.now().toString(),
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      }
      set((state) => ({
        projects: [...state.projects, newProject],
        isLoading: false,
      }))
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  updateProject: async (id: string, updates) => {
    set({ isLoading: true })
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 300))
      set((state) => ({
        projects: state.projects.map((p) =>
          p.id === id ? { ...p, ...updates, updatedAt: new Date().toISOString() } : p,
        ),
        currentProject:
          state.currentProject?.id === id
            ? { ...state.currentProject, ...updates, updatedAt: new Date().toISOString() }
            : state.currentProject,
        isLoading: false,
      }))
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  deleteProject: async (id: string) => {
    set({ isLoading: true })
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 300))
      set((state) => ({
        projects: state.projects.filter((p) => p.id !== id),
        currentProject: state.currentProject?.id === id ? null : state.currentProject,
        isLoading: false,
      }))
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },
}))
