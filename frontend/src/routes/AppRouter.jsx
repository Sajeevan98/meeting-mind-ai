import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "../pages/HomePage";
import NotFoundPage from "../pages/NotFoundPage";
import MainLayout from "../components/layout/MainLayout";

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
                        path="*"
                        element={<NotFoundPage />}
                    />
                </Routes>
            </MainLayout >
        </BrowserRouter>
    );
}