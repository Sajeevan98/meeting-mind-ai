import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "../pages/HomePage";
import NotFoundPage from "../pages/NotFoundPage";
import MainLayout from "../components/layout/MainLayout";
import MeetingListPage from "../pages/MeetingListPage";
import MeetingCreatePage from "../pages/MeetingCreatePage";
import MeetingDetailsPage from "../pages/MeetingDetailsPage";
import MeetingEditPage from "../pages/MeetingEditPage";

export default function AppRouter() {

    return (
        <BrowserRouter>
            <MainLayout>
                <Routes>

                    <Route
                        path="/"
                        element={<HomePage />}
                    />

                    <Route
                        path="/meetings"
                        element={<MeetingListPage />}
                    />

                    <Route
                        path="/meetings/new"
                        element={<MeetingCreatePage />}
                    />

                    <Route
                        path="/meetings/:uuid"
                        element={<MeetingDetailsPage />}
                    />

                    <Route
                        path="/meetings/:uuid/edit"
                        element={<MeetingEditPage />}
                    />

                    <Route
                        path="*"
                        element={<NotFoundPage />}
                    />

                </Routes>
            </MainLayout >
        </BrowserRouter>
    );
}