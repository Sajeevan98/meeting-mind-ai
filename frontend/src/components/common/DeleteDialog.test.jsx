import { render, screen } from '@testing-library/react'
import DeleteDialog from './DeleteDialog'
import userEvent from '@testing-library/user-event'

describe("DeleteDialog", () => {

    // Test renders the dialog
    it("should render meeting deletion confirmation", () => {

        const meeting = {
            title: "Sprint Planning"
        };

        render(
            <DeleteDialog
                open={true}
                property={meeting}
                loading={false}
                onClose={() => { }}
                onConfirm={() => { }}
                name="Meeting"
                errorMessage=""
            />
        );

        expect(
            screen.getByRole("heading", {
                name: "Delete Meeting"
            })
        ).toBeInTheDocument();

        expect(
            screen.getByText("Sprint Planning")
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", {
                name: "Cancel"
            })
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", {
                name: "Delete"
            })
        ).toBeInTheDocument();
    });

    // Test the user interaction for onClose
    it("should call onClose when cancel button is clicked", async () => {

        const user = userEvent.setup();

        const onClose = vi.fn();

        render(
            <DeleteDialog
                open={true}
                property={{ title: "Sprint Planning" }}
                loading={false}
                onClose={onClose}
                onConfirm={() => { }}
                name="Meeting"
                errorMessage=""
            />
        );

        await user.click(
            screen.getByRole("button", {
                name: "Cancel"
            })
        );

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    // Test the user interaction for onConfirm
    it("should call onConfirm when delete button is clicked", async () => {

        const user = userEvent.setup();

        const onConfirm = vi.fn();

        render(
            <DeleteDialog
                open={true}
                property={{ title: "Sprint Planning" }}
                loading={false}
                onClose={() => { }}
                onConfirm={onConfirm}
                name="Meeting"
                errorMessage=""
            />
        );

        await user.click(
            screen.getByRole("button", {
                name: "Delete"
            })
        );

        expect(onConfirm).toHaveBeenCalledTimes(1);
    });

    // Test the loading state
    it("should disable buttons and show deleting text while loading", () => {

        render(
            <DeleteDialog
                open={true}
                property={{ title: "Sprint Planning" }}
                loading={true}
                onClose={() => { }}
                onConfirm={() => { }}
                name="Meeting"
                errorMessage=""
            />
        );

        expect(
            screen.getByRole("button", {
                name: "Deleting..."
            })
        ).toBeDisabled();

        expect(
            screen.getByRole("button", {
                name: "Cancel"
            })
        ).toBeDisabled();
    });

    // Test error message
    it("should display error message", () => {

        render(
            <DeleteDialog
                open={true}
                property={{ title: "Sprint Planning" }}
                loading={false}
                onClose={() => { }}
                onConfirm={() => { }}
                name="Meeting"
                errorMessage="Failed to delete meeting."
            />
        );

        expect(
            screen.getByText("Failed to delete meeting.")
        ).toBeInTheDocument();
    });

});