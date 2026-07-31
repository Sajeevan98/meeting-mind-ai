import MenuIcon from "@mui/icons-material/Menu";

import { AppBar, Toolbar, Typography, IconButton } from "@mui/material";

export default function AppNavbar({ onDrawerToggle }) {

    return (
        <AppBar>
            <Toolbar>

                <IconButton
                    color="inherit"
                    edge="start"
                    onClick={onDrawerToggle}
                    sx={{
                        mr: 2,
                        display: {
                            md: "none"
                        }
                    }}
                >
                    <MenuIcon />
                </IconButton>

                <Typography
                    variant="h6"
                    sx={{ flexGrow: 1 }}
                >
                    MeetingMind AI
                </Typography>

            </Toolbar>
        </AppBar>
    );
}