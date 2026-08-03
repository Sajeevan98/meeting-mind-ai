import { Box, Tab, Tabs } from "@mui/material"
import { useSearchParams } from "react-router-dom";

const MeetingDetailsTabs = () => {

    const [searchParams, setSearchParams] = useSearchParams();

    const currentTab = searchParams.get("tab") || "overview";

    const handleChange = (_, newValue) => {

        setSearchParams({ tab: newValue });
    };


    return (
        <Box
            sx={{ borderBottom: 1, borderColor: "divider", marginBottom: 4}}
        >
            <Tabs
                value={currentTab}
                onChange={handleChange}
            >
                <Tab
                    label="Overview"
                    value="overview"
                />
                <Tab
                    label="Attachments"
                    value="attachments"
                />
                <Tab
                    label="AI Analysis"
                    value="analysis"
                />
            </Tabs>
        </Box>
    )
}

export default MeetingDetailsTabs