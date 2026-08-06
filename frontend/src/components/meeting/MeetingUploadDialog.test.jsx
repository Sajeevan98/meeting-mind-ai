import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { SnackbarProvider } from 'notistack'
import MeetingUploadDialog from './MeetingUploadDialog'
import { useCreateAttachment } from '../../hooks/useAttachment'

// Mocks
vi.mock("../../hooks/useAttachment", () => ({
    useCreateAttachment: vi.fn()
}));

const mockEnqueueSnackbar = vi.fn();

vi.mock("notistack", async () => {

    const actual = await vi.importActual("notistack");

    return {
        ...actual,
        useSnackbar: () => ({
            enqueueSnackbar: mockEnqueueSnackbar
        })
    };
});

// Test data
const meetingUuid = "123e4567-e89b-12d3-a456-426614174000";

const renderDialog = (props = {}) => {

    return render(
        <SnackbarProvider>
            <MeetingUploadDialog
                open={true}
                meetingUuid={meetingUuid}
                onClose={vi.fn()}
                {...props}
            />
        </SnackbarProvider>
    );
};


// Tests
describe("MeetingUploadDialog", () => {

    let mutateMock;

    beforeEach(() => {

        vi.clearAllMocks();

        mutateMock = vi.fn();

        useCreateAttachment.mockReturnValue({
            mutate: mutateMock,
            isPending: false
        });
    });


    it("should render upload dialog", () => {

        renderDialog();

        expect(
            screen.getByText("Upload Meeting Attachment")
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", {
                name: /select file/i
            })
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", {
                name: /upload/i
            })
        ).toBeDisabled();

        expect(
            screen.getByRole("button", {
                name: /cancel/i
            })
        ).toBeEnabled();
    });

    it("should disable upload button when no file is selected", () => {

        render(
            <MeetingUploadDialog
                open={true}
                meetingUuid="meeting-123"
                onClose={vi.fn()}
            />
        );

        expect(
            screen.getByRole("button", {
                name: /^upload$/i
            })
        ).toBeDisabled();
    });

    it("should display selected file", () => {

        renderDialog();

        const file = new File(
            ["meeting content"],
            "meeting-notes.pdf",
            {
                type: "application/pdf"
            }
        );

        const input = screen.getByLabelText(/select file/i);

        fireEvent.change(input, {
            target: {
                files: [file]
            }
        });

        expect(
            screen.getByText("meeting-notes.pdf")
        ).toBeInTheDocument();
    });


    it("should enable upload button after selecting a file", () => {

        renderDialog();

        const uploadButton = screen.getByRole("button", {
            name: /upload/i
        });

        expect(uploadButton).toBeDisabled();

        const file = new File(
            ["meeting content"],
            "meeting-notes.pdf",
            {
                type: "application/pdf"
            }
        );

        const input = screen.getByLabelText(/select file/i);

        fireEvent.change(input, {
            target: {
                files: [file]
            }
        });

        expect(uploadButton).toBeEnabled();
    });


    it("should call mutation with FormData when upload is clicked", () => {

        renderDialog();

        const file = new File(
            ["meeting content"],
            "meeting-notes.pdf",
            {
                type: "application/pdf"
            }
        );

        const input = screen.getByLabelText(/select file/i);

        fireEvent.change(input, {
            target: {
                files: [file]
            }
        });

        fireEvent.click(
            screen.getByRole("button", {
                name: /upload/i
            })
        );

        expect(mutateMock).toHaveBeenCalledTimes(1);

        const [formData] = mutateMock.mock.calls[0];

        expect(formData).toBeInstanceOf(FormData);

        expect(formData.get("uuid")).toBe(meetingUuid);
        expect(formData.get("file")).toBe(file);
    });


    it("should handle successful upload", async () => {

        const onClose = vi.fn();

        renderDialog({
            onClose
        });

        const file = new File(
            ["meeting content"],
            "meeting-notes.pdf",
            {
                type: "application/pdf"
            }
        );

        const input = screen.getByLabelText(/select file/i);

        fireEvent.change(input, {
            target: {
                files: [file]
            }
        });

        fireEvent.click(
            screen.getByRole("button", {
                name: /upload/i
            })
        );

        const [, options] = mutateMock.mock.calls[0];

        options.onSuccess();

        await waitFor(() => {

            expect(
                mockEnqueueSnackbar
            ).toHaveBeenCalledWith(
                "File uploaded successfully.",
                {
                    variant: "success"
                }
            );

            expect(onClose).toHaveBeenCalledTimes(1);
        });
    });


    it("should display server error when upload fails", async () => {

        renderDialog();

        const file = new File(
            ["meeting content"],
            "meeting-notes.pdf",
            {
                type: "application/pdf"
            }
        );

        const input = screen.getByLabelText(/select file/i);

        fireEvent.change(input, {
            target: {
                files: [file]
            }
        });

        fireEvent.click(
            screen.getByRole("button", {
                name: /upload/i
            })
        );

        const [, options] = mutateMock.mock.calls[0];

        options.onError({
            response: {
                data: {
                    message: "File type is not supported."
                }
            }
        });

        await waitFor(() => {

            expect(
                screen.getByText("File type is not supported.")
            ).toBeInTheDocument();
        });
    });


    it("should show uploading state and disable actions while upload is pending", () => {

        useCreateAttachment.mockReturnValue({
            mutate: mutateMock,
            isPending: true
        });

        renderDialog();

        expect(
            screen.getByRole("button", {
                name: /uploading/i
            })
        ).toBeDisabled();

        expect(
            screen.getByRole("button", {
                name: /cancel/i
            })
        ).toBeDisabled();

        expect(
            screen.getByRole("button", {
                name: /select file/i
            })
        ).toHaveAttribute("aria-disabled", "true");
    });
});