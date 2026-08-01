import { Drawer, Toolbar, List, ListItemButton, ListItemText } from "@mui/material";
import { DRAWER_WIDTH } from "./layoutConstants";
import { NavLink } from "react-router-dom";

export default function AppSidebar({ mobileOpen, onDrawerToggle }) {

    const drawer = (

        <>
            <Toolbar />
            <List>

                <ListItemButton 
                    onClick={onDrawerToggle}
                    component={NavLink}
                    to="/"
                >
                    <ListItemText primary="Dashboard" />
                </ListItemButton>

                <ListItemButton
                    onClick={onDrawerToggle}
                    component={NavLink}
                    to="/meetings"
                >
                    <ListItemText primary="Meetings" />
                </ListItemButton>

                <ListItemButton onClick={onDrawerToggle}>
                    <ListItemText primary="Analysis" />
                </ListItemButton>

                <ListItemButton onClick={onDrawerToggle}>
                    <ListItemText primary="Settings" />
                </ListItemButton>

            </List>
        </>
    );

    return (
        <>
            <Drawer
                variant="temporary"

                open={mobileOpen}

                onClose={onDrawerToggle}

                sx={{

                    display: {
                        xs: "block",
                        md: "none"
                    },

                    "& .MuiDrawer-paper": {

                        width: DRAWER_WIDTH
                    }
                }}
            >
                {drawer}
            </Drawer>

            <Drawer
                variant="permanent"
                sx={{

                    display: {
                        xs: "none",
                        md: "block"
                    },

                    width: DRAWER_WIDTH,
                    flexShrink: 0,

                    "& .MuiDrawer-paper": {

                        width: DRAWER_WIDTH,
                        boxSizing: "border-box"
                    }
                }}
            >
                {drawer}
            </Drawer>
        </>
    );
}