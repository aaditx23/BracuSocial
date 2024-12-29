import React, { useState } from 'react';
import UploadImage from './uploadImage';

interface ImagePreviewProps {
  base64String: string;
  showModal?: boolean; // Controls if modal functionality is enabled
  size?: Number
}

const ImagePreview: React.FC<ImagePreviewProps> = ({
  base64String,
  showModal = false, // Default: modal is disabled
  size = 32
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleImageClick = () => {
    if (showModal) {
      setIsModalOpen(true); // Open the modal only if showModal is true
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false); // Close the modal
  };

  return (
    <div className="flex justify-center items-center">
      <div
        className={`rounded-full overflow-hidden border-2 border-gray-300 flex justify-center items-center cursor-pointer`}
        style={{ width: `${size}px`, height: `${size}px` }} // Dynamic width and height
        onClick={handleImageClick} // Trigger modal on click
      >
        {base64String !== "" ? (
          <img
            src={`${base64String}`}
            alt="Profile Preview"
            className="w-full h-full object-cover"
          />
        ) : (
          <p className="text-center text-sm text-gray-500">No Image</p>
        )}
      </div>

      {/* Conditionally render the UploadImage modal */}
      {isModalOpen && (
        <UploadImage isOpen={isModalOpen} onClose={handleCloseModal} />
      )}
    </div>
  );
};

export default ImagePreview;
