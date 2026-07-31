import { useState } from "react";
import { Box, Toolbar } from "@mui/material";
import AppNavbar from "./AppNavbar";
import AppSidebar from "./AppSidebar";
import { DRAWER_WIDTH } from "./layoutConstants";

export default function MainLayout({ children }) {

    const [mobileOpen, setMobileOpen] = useState(false);

    const handleDrawerToggle = () => {
        setMobileOpen(!mobileOpen);
    };

    return (

        <Box sx={{ display: "flex" }}>

            <AppNavbar onDrawerToggle={handleDrawerToggle}/>

            <AppSidebar mobileOpen={mobileOpen} onDrawerToggle={handleDrawerToggle}/>

            <Box
                component="main"
                sx={{

                    flexGrow: 1,
                    p: 3,
                    width: {
                        md: `calc(100% - ${DRAWER_WIDTH}px)`    
                    }
                }}
            >
                <Toolbar />

                {children}

            </Box>

        </Box>
    );
}