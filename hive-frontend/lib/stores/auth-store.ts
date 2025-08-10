import { create } from "zustand"
import { persist } from "zustand/middleware"

interface User {
  id: string
  firstName: string
  lastName: string
  email: string
  role: "PROJECT_ADMIN" | "PROJECT_LEADER" | "TEAM_MEMBER"
  organization: string
}

interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  register: (userData: {
    firstName: string
    lastName: string
    email: string
    password: string
    organization: string
  }) => Promise<void>
  logout: () => void
  refreshToken: () => Promise<void>
}

// Update the base API URL
const API_BASE_URL = "http://localhost:9999"

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      isAuthenticated: false,

      login: async (email: string, password: string) => {
        try {
          // Simulate API call
          const response = await fetch(`${API_BASE_URL}/api/v1/auth/login`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, password }),
          })

          if (!response.ok) {
            throw new Error("Login failed")
          }

          const data = await response.json()

          // Mock successful login
          const mockUser: User = {
            id: "1",
            firstName: "John",
            lastName: "Doe",
            email: email,
            role: "PROJECT_LEADER",
            organization: "Acme Corp",
          }

          set({
            user: mockUser,
            token: "mock-jwt-token",
            isAuthenticated: true,
          })
        } catch (error) {
          throw error
        }
      },

      register: async (userData) => {
        try {
          // Simulate API call
          const response = await fetch(`${API_BASE_URL}/api/v1/auth/register`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
          })

          if (!response.ok) {
            throw new Error("Registration failed")
          }

          // Mock successful registration
          const mockUser: User = {
            id: "1",
            firstName: userData.firstName,
            lastName: userData.lastName,
            email: userData.email,
            role: "TEAM_MEMBER", // Default role is TEAM_MEMBER
            organization: userData.organization,
          }

          set({
            user: mockUser,
            token: "mock-jwt-token",
            isAuthenticated: true,
          })
        } catch (error) {
          throw error
        }
      },

      logout: () => {
        set({
          user: null,
          token: null,
          isAuthenticated: false,
        })
      },

      refreshToken: async () => {
        try {
          const { token } = get()
          if (!token) return

          const response = await fetch(`${API_BASE_URL}/api/v1/auth/refresh`, {
            method: "POST",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })

          if (!response.ok) {
            throw new Error("Token refresh failed")
          }

          const data = await response.json()
          set({ token: data.token })
        } catch (error) {
          // If refresh fails, logout user
          get().logout()
        }
      },
    }),
    {
      name: "auth-storage",
    },
  ),
)
