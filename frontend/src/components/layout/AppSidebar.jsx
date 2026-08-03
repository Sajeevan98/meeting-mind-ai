import { Drawer, Toolbar, List, ListItemButton, ListItemText, Typography } from "@mui/material";
import { DRAWER_WIDTH } from "./layoutConstants";
import { NavLink } from "react-router-dom";

export default function AppSidebar({ mobileOpen, onDrawerToggle }) {

    const navItemSx = {
        mx: 1,
        borderRadius: 1,
        color: "text.primary",

        "&.active": {
            bgcolor: "primary.main",
            color: "primary.contrastText",
        },

        "&.active:hover": {
            bgcolor: "primary.dark",
        },
    };

    const drawer = (
        <>
            
            <Typography color="primary" sx={{fontSize: 24, fontWeight: 600, marginX: 'auto', marginY: 3}} >MeetingMind AI</Typography>
            {/* <Toolbar /> */}
            <List>
                <ListItemButton
                    component={NavLink}
                    to="/"
                    end
                    onClick={onDrawerToggle}
                    sx={navItemSx}
                >
                    <ListItemText primary="Dashboard" />
                </ListItemButton>

                <ListItemButton
                    component={NavLink}
                    to="/meetings"
                    onClick={onDrawerToggle}
                    sx={navItemSx}
                >
                    <ListItemText primary="Meetings" />
                </ListItemButton>

                <ListItemButton onClick={onDrawerToggle}>
                    <ListItemText primary="Features" />
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