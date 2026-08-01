import { Typography, Grid, Card, CardContent } from "@mui/material";

export default function HomePage() {

  return (

    <>
      <Typography
        variant="h4"
        gutterBottom
        color="primary"
      >
        Dashboard
      </Typography>

      <Grid container spacing={3}>
        {[

          "Meetings",
          "Documents",
          "Analyses"

        ].map(title => (

          <Grid
            key={title}
            size={{ xs: 12, md: 4 }}
          >
            <Card>
              <CardContent>

                <Typography variant="h6" color="textPrimary">
                  {title}
                </Typography>

                <Typography variant="h4" color="textSecondary">
                  0
                </Typography>

              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </>
  );
}