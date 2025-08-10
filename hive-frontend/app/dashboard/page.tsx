"use client"
import { useAuthStore } from "@/lib/stores/auth-store"
import { Layout } from "@/components/layout"
import { AdminDashboard } from "@/components/dashboards/admin-dashboard"
import { LeaderDashboard } from "@/components/dashboards/leader-dashboard"
import { MemberDashboard } from "@/components/dashboards/member-dashboard"

export default function DashboardPage() {
  const { user } = useAuthStore()

  if (!user) {
    return (
      <Layout>
        <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 flex items-center justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#d3bc8d]"></div>
        </div>
      </Layout>
    )
  }

  // Render different dashboards based on user role
  switch (user.role) {
    case "PROJECT_ADMIN":
      return (
        <Layout>
          <AdminDashboard />
        </Layout>
      )
    case "PROJECT_LEADER":
      return (
        <Layout>
          <LeaderDashboard />
        </Layout>
      )
    case "TEAM_MEMBER":
      return (
        <Layout>
          <MemberDashboard />
        </Layout>
      )
    default:
      return (
        <Layout>
          <MemberDashboard />
        </Layout>
      )
  }
}
