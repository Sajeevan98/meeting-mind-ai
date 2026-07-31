import { Typography, Grid, Card, CardContent } from "@mui/material";

export default function HomePage() {

  return (

    <>
      <Typography
        variant="h4"
        gutterBottom
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

                <Typography variant="h6">
                  {title}
                </Typography>

                <Typography variant="h4">
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